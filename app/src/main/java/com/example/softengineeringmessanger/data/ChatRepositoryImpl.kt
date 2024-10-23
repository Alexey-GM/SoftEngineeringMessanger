package com.example.softengineeringmessanger.data

import com.example.softengineeringmessanger.data.local.ChatDB
import com.example.softengineeringmessanger.data.local.MessageDB
import com.example.softengineeringmessanger.data.nw.MessengerApiService
import com.example.softengineeringmessanger.data.nw.UserManager
import com.example.softengineeringmessanger.data.nw.model.MessageRequest
import com.example.softengineeringmessanger.domain.ChatRepository
import com.example.softengineeringmessanger.domain.model.Chat
import com.example.softengineeringmessanger.domain.model.Message
import io.realm.Realm

class ChatRepositoryImpl(
    private val messengerApiService: MessengerApiService,
    private val realm: Realm,
    private val userManager: UserManager,
) : ChatRepository {
    override suspend fun sendMessage(to: Int, message: String): Result<String> {
        return try {
            val messageRequest = MessageRequest(
                to = to,
                message = message
            )
            val response = messengerApiService.sendMessage(messageRequest, userManager.getUserToken() ?: "")

            if (response.isSuccessful && response.body() != null) {
                realm.executeTransaction { r ->
                    val messageDB = r.createObject(MessageDB::class.java, response.body()!!.id).apply {
                        this.date = response.body()!!.date
                        this.delivered = response.body()!!.delivered
                        this.message = response.body()!!.message
                        this.to = response.body()!!.to
                    }
                    val chat = r.where(ChatDB::class.java).equalTo("participantId", to).findFirst()
                        ?: r.createObject(ChatDB::class.java, to)
                    chat.messages.add(messageDB)
                }
                Result.success("Message sent and saved locally")
            } else {
                Result.failure(Exception("Failed to send message"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessages(from: Int): Result<List<Message>> {
        return try {
            val response = messengerApiService.getMessages(from, userManager.getUserToken() ?: "")

            if (response.isSuccessful && response.body() != null) {
                val messagesFromApi = response.body()!!

                realm.executeTransaction { r ->
                    for (apiMessage in messagesFromApi) {
                        val existingMessage = r.where(MessageDB::class.java)
                            .equalTo("id", apiMessage.id)
                            .findFirst()

                        if (existingMessage == null) {
                            val newMessageDB = r.createObject(MessageDB::class.java, apiMessage.id).apply {
                                this.date = apiMessage.date
                                this.delivered = apiMessage.delivered
                                this.message = apiMessage.message
                                this.to = apiMessage.to
                            }

                            val chat = r.where(ChatDB::class.java).equalTo("participantId", from).findFirst()
                                ?: r.createObject(ChatDB::class.java, from)
                            chat.messages.add(newMessageDB)
                        }
                    }
                }

                val messages = messagesFromApi.map { apiMessage ->
                    Message(
                        id = apiMessage.id,
                        date = apiMessage.date,
                        delivered = apiMessage.delivered,
                        message = apiMessage.message,
                        to = apiMessage.to
                    )
                }
                Result.success(messages)
            } else {
                Result.failure(Exception("Failed to fetch messages"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChats(): Result<List<Chat>> {
        return try {
            val chats = realm.where(ChatDB::class.java).findAll().map { chatDB ->
                Chat(
                    id = chatDB.id,
                    participantId = chatDB.participantId,
                    messages = chatDB.messages.map { messageDB ->
                        Message(
                            id = messageDB.id,
                            date = messageDB.date,
                            delivered = messageDB.delivered,
                            message = messageDB.message,
                            to = messageDB.to
                        )
                    }
                )
            }
            Result.success(chats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
