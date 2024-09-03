package com.techlambda.authlibrary.ui.signUp

import com.techlambda.authlibrary.ui.models.ApiResponse
import com.techlambda.authlibrary.ui.models.CodeVerificationRequest
import com.techlambda.authlibrary.ui.models.CodeVerificationResponse
import com.techlambda.authlibrary.ui.models.OtpRequest
import com.techlambda.authlibrary.ui.models.ResetPasswordRequest
import com.techlambda.authlibrary.ui.models.SignInRequest
import com.techlambda.authlibrary.ui.models.SignUpRequest
import com.techlambda.authlibrary.ui.models.SignUpResponse
import com.techlambda.authlibrary.ui.utils.NetworkResult
import com.techlambda.authlibrary.ui.utils.makeApiCall
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiService
) {
    //    private val api = RetrofitInstance.api
    suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<ApiResponse<SignUpResponse>> {
        return makeApiCall({ api.signUp(signUpRequest) }, signUpRequest)
    }

    suspend fun signIn(signInRequest: SignInRequest): NetworkResult<ApiResponse<SignUpResponse>> {
        return makeApiCall({ api.signIn(signInRequest) }, signInRequest)
    }

    suspend fun sendOtp(otpRequest: OtpRequest): NetworkResult<ApiResponse<Any>> {
        return makeApiCall({ api.sendOtp(otpRequest) }, otpRequest)
    }

    suspend fun resendOtp(otpRequest: OtpRequest): NetworkResult<ApiResponse<Any>> {
        return makeApiCall({ api.resendOtp(otpRequest) }, otpRequest)
    }

    suspend fun verifyOtp(otpRequest: OtpRequest): NetworkResult<ApiResponse<Any>> {
        return makeApiCall({ api.verifyOtp(otpRequest) }, otpRequest)
    }

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): NetworkResult<ApiResponse<Any>> {
        return makeApiCall({ api.resetPassword(resetPasswordRequest) }, resetPasswordRequest)
    }
    suspend fun verifyCode(codeVerificationRequest: CodeVerificationRequest): NetworkResult<ApiResponse<CodeVerificationResponse>> {
        return makeApiCall({ api.verifyCode(codeVerificationRequest) }, codeVerificationRequest)
    }
}