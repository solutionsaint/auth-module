package com.techlambda.onlineeducation.repository.auth

import com.techlambda.onlineeducation.network.AuthApiService
import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignUpRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignUpResponseModel
import com.techlambda.onlineeducation.utils.ApiResponse
import com.techlambda.onlineeducation.utils.safeApiCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApiService: AuthApiService): AuthRepository {

    override suspend fun login(loginRequestModel: LoginRequestModel): ApiResponse<LoginResponseModel> {
        return safeApiCall {
            authApiService.login(loginRequestModel)
        }
    }

    override suspend fun signUp(signUpRequestModel: SignUpRequestModel): ApiResponse<SignUpResponseModel> {
        return safeApiCall {
            authApiService.signUp(signUpRequestModel)
        }
    }

}
