package com.techlambda.onlineeducation.data.local.preferences

import com.techlambda.onlineeducation.data.local.preferences.models.UserData

interface DataStoreManager {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?

    suspend fun getUserRole(): DataStoreManagerImpl.Role?
    suspend fun saveUserRole(role: DataStoreManagerImpl.Role)

    suspend fun saveUserData(userData: UserData)
    suspend fun getUserData(): UserData?
}
