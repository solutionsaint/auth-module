package com.techlambda.onlineeducation.repository.auth

import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignUpRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignUpResponseModel
import com.techlambda.onlineeducation.utils.ApiResponse

interface AuthRepository {
    suspend fun login(loginRequestModel: LoginRequestModel): ApiResponse<LoginResponseModel>
    suspend fun signUp(signUpRequestModel: SignUpRequestModel): ApiResponse<SignUpResponseModel>
}