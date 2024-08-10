package com.techlambda.onlineeducation.repository.auth

import AuthApiService
import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignupRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignupResponseModel
import com.techlambda.onlineeducation.utils.ApiResponse
import com.techlambda.onlineeducation.utils.safeApiCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApiService: AuthApiService): AuthRepository {

    override suspend fun login(loginRequestModel: LoginRequestModel): ApiResponse<LoginResponseModel> {
        return safeApiCall {
            authApiService.login(loginRequestModel)
        }
    }

    override suspend fun signUp(signUpRequestModel: SignupRequestModel): ApiResponse<SignupResponseModel> {
        return safeApiCall {
            authApiService.signUp(signUpRequestModel)
        }
    }

}
