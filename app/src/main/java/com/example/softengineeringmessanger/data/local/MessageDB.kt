package com.example.softengineeringmessanger.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MessageDB(
    @PrimaryKey var id: Int = 0,
    var date: Long = 0L,
    var delivered: Boolean = false,
    var message: String = "",
    var to: Int = 0
) : RealmObject()
