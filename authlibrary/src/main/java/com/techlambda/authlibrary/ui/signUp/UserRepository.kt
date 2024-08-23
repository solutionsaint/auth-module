package com.techlambda.authlibrary.ui.signUp

import com.techlambda.authlibrary.ui.signUp.ApiResponse
import com.techlambda.authlibrary.ui.signUp.ApiService
import com.techlambda.authlibrary.ui.signUp.OtpRequest
import com.techlambda.authlibrary.ui.signUp.ResetPasswordRequest
import com.techlambda.authlibrary.ui.signUp.SignInRequest
import com.techlambda.authlibrary.ui.signUp.SignUpRequest
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiService
) {
//    private val api = RetrofitInstance.api
    suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<Any> {
        return api.signUp(signUpRequest)
    }

    suspend fun signIn(signInRequest: SignInRequest): ApiResponse<Any> {
        return api.signIn(signInRequest)
    }

    suspend fun sendOtp(otpRequest: OtpRequest): ApiResponse<Any> {
        return api.sendOtp(otpRequest)
    }

    suspend fun resendOtp(otpRequest: OtpRequest): ApiResponse<Any> {
        return api.resendOtp(otpRequest)
    }

    suspend fun verifyOtp(otpRequest: OtpRequest): ApiResponse<Any> {
        return api.verifyOtp(otpRequest)
    }

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ApiResponse<Any> {
        return api.resetPassword(resetPasswordRequest)
    }
}