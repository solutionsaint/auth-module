package com.techlambda.onlineeducation.repository.auth

import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignupRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignupResponseModel
import com.techlambda.onlineeducation.utils.ApiResponse

interface AuthRepository {
    suspend fun login(loginRequestModel: LoginRequestModel): ApiResponse<LoginResponseModel>
    suspend fun signUp(signUpRequestModel: SignupRequestModel): ApiResponse<SignupResponseModel>
}