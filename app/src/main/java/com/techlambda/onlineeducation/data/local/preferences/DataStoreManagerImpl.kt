package com.techlambda.onlineeducation.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.techlambda.onlineeducation.data.local.preferences.models.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManagerImpl(context: Context) : DataStoreManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_preferences")

    private val dataStore = context.dataStore

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val FULL_NAME_KEY = stringPreferencesKey("name")
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }

    override suspend fun saveUserRole(role: Role) {
        dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role.name
        }
    }

    override suspend fun saveUserData(userData: UserData) {

    }

    override suspend fun getUserData(): UserData? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserRole(): Role? {
        val name =  dataStore.data.map { preferences ->
            preferences[USER_ROLE_KEY]
        }.first()

        return when(name){
            Role.STUDENT.name -> Role.STUDENT
            Role.INSTRUCTOR.name -> Role.INSTRUCTOR
            else -> null
        }
    }

    enum class Role{
        STUDENT,INSTRUCTOR
    }
}
