package com.example.softengineeringmessanger.data.nw

import android.content.SharedPreferences
import javax.inject.Inject

class UserManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_USER_ID = "user_id"
    }

    fun saveUserToken(token: String) {
        sharedPreferences.edit().putString(KEY_USER_TOKEN, token).apply()
    }

    fun getUserToken(): String? {
        return sharedPreferences.getString(KEY_USER_TOKEN, null)
    }

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }
}
