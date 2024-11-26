package com.techlambda.authlibrary.ui.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthPrefManager @Inject constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = Json {
        ignoreUnknownKeys = true
    }

    // Key for storing UserData
    private val USER_DATA = "user_data"
    private val FCM_TOKEN = "fcm_token"

    // Function to save UserData
    fun saveUserData(userData: UserData) {
        val jsonString = gson.encodeToString(userData)
        sharedPreferences.edit()
            .putString(USER_DATA, jsonString)
            .apply()
    }

    // Function to retrieve UserData
    fun getUserData(): UserData? {
        val jsonString = sharedPreferences.getString(USER_DATA, null)
        return jsonString?.let {
            try {
                gson.decodeFromString<UserData>(it)
            } catch (e: Exception) {
                null
            }
        }
    }
    fun saveFCMToken(token: String) {
        sharedPreferences.edit()
            .putString(FCM_TOKEN, token)
            .apply()
    }

    fun getFCMToken(): String? {
        return sharedPreferences.getString(FCM_TOKEN, null)
    }

    // Function to check if user is logged in
    fun isLoggedIn(): Boolean {
        return getUserData() != null
    }

    // Function to clear UserData
    fun clearUserData() {
        sharedPreferences.edit()
            .remove(USER_DATA)
            .remove(FCM_TOKEN)
            .apply()
    }
}