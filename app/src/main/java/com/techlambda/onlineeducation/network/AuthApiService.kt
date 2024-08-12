package com.techlambda.onlineeducation.network

import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignUpRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignUpResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthApiService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun login(loginRequestModel: LoginRequestModel): LoginResponseModel {
        return httpClient.post("/auth/login") {
            setBody(loginRequestModel)
        }.body()
    }


    suspend fun signUp(signUpRequestModel: SignUpRequestModel): SignUpResponseModel {
        return httpClient.post("/auth/signup") {
            setBody(signUpRequestModel)
        }.body()
    }
}

