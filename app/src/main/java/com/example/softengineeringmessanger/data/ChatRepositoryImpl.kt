package com.example.softengineeringmessanger.data

import android.util.Log
import com.example.softengineeringmessanger.data.local.dao.ChatDao
import com.example.softengineeringmessanger.data.local.dao.MessageDao
import com.example.softengineeringmessanger.data.local.model.ChatDB
import com.example.softengineeringmessanger.data.local.model.MessageDB
import com.example.softengineeringmessanger.data.nw.MessengerApiService
import com.example.softengineeringmessanger.data.nw.UserManager
import com.example.softengineeringmessanger.data.nw.model.MessageRequest
import com.example.softengineeringmessanger.domain.ChatRepository
import com.example.softengineeringmessanger.domain.model.Chat
import com.example.softengineeringmessanger.domain.model.Message

class ChatRepositoryImpl(
    private val messengerApiService: MessengerApiService,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val userManager: UserManager
) : ChatRepository {

    override suspend fun sendMessage(to: Int, message: String): Result<String> {
        return try {
            val messageRequest = MessageRequest(to = to, message = message)
            val response =
                messengerApiService.sendMessage(messageRequest, userManager.getUserToken() ?: "")
            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                val chat = chatDao.findChat(to) ?: ChatDB(participantId = to).also {
                    chatDao.insertChat(it)
                }
                val messageDB = MessageDB(
                    id = responseBody.id,
                    date = responseBody.date,
                    delivered = responseBody.delivered,
                    message = responseBody.message,
                    to = responseBody.to,
                    chatId = chat.id
                )
                messageDao.insertMessage(messageDB)
                Result.success("Message sent and saved locally")
            } else {
                Result.failure(Exception("Failed to send message"))
            }
        } catch (e: Exception) {
            Log.e("ChatRepositoryImpl", "Error sending message", e)
            Result.failure(e)
        }
    }

    override suspend fun getMessages(from: Int): Result<List<Message>> {
        return try {
            val response = messengerApiService.getMessages(from, userManager.getUserToken() ?: "")
            if (response.isSuccessful && response.body() != null) {
                val messagesFromApi = response.body()!!
                if (messagesFromApi.isEmpty()) {
                    return Result.success(emptyList())
                }
                val chat = chatDao.findChat(from) ?: run {
                    val newChat = ChatDB(participantId = from)
                    chatDao.insertChat(newChat)
                    newChat
                }
                val existingMessageIds =
                    messageDao.getMessagesByChatId(chat.id).map { it.id }.toSet()
                val messagesToInsert =
                    messagesFromApi.filterNot { existingMessageIds.contains(it.id) }
                messagesToInsert.forEach { apiMessage ->
                    val messageDB = MessageDB(
                        id = apiMessage.id,
                        date = apiMessage.date,
                        delivered = apiMessage.delivered,
                        message = apiMessage.message,
                        to = apiMessage.to,
                        chatId = chat.id
                    )
                    messageDao.insertMessage(messageDB)
                }
                val allMessages = messageDao.getMessagesByChatId(chat.id).map { messageDB ->
                    Message(
                        id = messageDB.id,
                        date = messageDB.date,
                        delivered = messageDB.delivered,
                        message = messageDB.message,
                        to = messageDB.to
                    )
                }
                Result.success(allMessages)
            } else {
                Result.failure(Exception("Failed to fetch messages from server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChats(): Result<List<Chat>> {
        return try {
            val chats = chatDao.getChats().map { chatDB ->
                // Fetch the last message for each chat
                val lastMessageDB = messageDao.getMessagesByChatId(chatDB.id).lastOrNull()
                val lastMessage = lastMessageDB?.let { messageDB ->
                    Message(
                        id = messageDB.id,
                        date = messageDB.date,
                        delivered = messageDB.delivered,
                        message = messageDB.message,
                        to = messageDB.to
                    )
                }
                Chat(
                    id = chatDB.id,
                    participantId = chatDB.participantId,
                    participantLogin = "", // You should retrieve this from the appropriate source
                    lastMessage = lastMessage ?: Message(
                        0,
                        0L,
                        false,
                        "",
                        0
                    ) // Handle no last message
                )
            }
            Result.success(chats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun receiveMessagesFromServer(from: Int): Result<String> {
        return try {
            val response = messengerApiService.getMessages(from, userManager.getUserToken() ?: "")

            if (response.isSuccessful && response.body() != null) {
                val messagesFromApi = response.body()!!

                val chat = chatDao.findChat(from) ?: ChatDB(participantId = from).also {
                    chatDao.insertChat(it)
                }

                messagesFromApi.forEach { apiMessage ->
                    if (messageDao.findMessageById(apiMessage.id) == null) {
                        val messageDB = MessageDB(
                            id = apiMessage.id,
                            date = apiMessage.date,
                            delivered = apiMessage.delivered,
                            message = apiMessage.message,
                            to = apiMessage.to,
                            chatId = chat.id
                        )
                        messageDao.insertMessage(messageDB)
                    }
                }

                Result.success("Messages received and saved locally")
            } else {
                Result.failure(Exception("Failed to receive messages"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
