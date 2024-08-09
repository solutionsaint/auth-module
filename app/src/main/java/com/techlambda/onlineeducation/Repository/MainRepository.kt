package com.techlambda.onlineeducation.Repository

import AuthApi
import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun login(username: String, password: String, email: String): Result<LoginResponseModel> {
        return try {
            val request = LoginRequestModel(username, password, email)
            val response = authApi.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
