package com.example.softengineeringmessanger.data.local

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ChatDB(
    @PrimaryKey var id: Int = 0,
    var participantId: Int = 0,
    var messages: RealmList<MessageDB> = RealmList()
) : RealmObject()