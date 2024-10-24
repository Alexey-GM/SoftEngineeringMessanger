package com.example.softengineeringmessanger.data.nw.model


import com.google.gson.annotations.SerializedName

class UserList : ArrayList<UserListItem>()

data class UserListItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String
)