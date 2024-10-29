package com.techlambda.authlibrary.ui.signUp

import com.techlambda.authlibrary.ui.models.ApiResponse
import com.techlambda.authlibrary.ui.models.CodeVerificationResponse
import com.techlambda.authlibrary.ui.models.OtpRequest
import com.techlambda.authlibrary.ui.models.ResetPasswordRequest
import com.techlambda.authlibrary.ui.models.SignInRequest
import com.techlambda.authlibrary.ui.models.SignUpRequest
import com.techlambda.authlibrary.ui.models.SignUpResponse
import com.techlambda.authlibrary.ui.models.UpdateProfileRequest
import com.techlambda.authlibrary.ui.models.VerifyUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<ApiResponse<SignUpResponse>>

    @POST("auth/signin")
    suspend fun signIn(@Body signInRequest: SignInRequest):  Response<ApiResponse<SignUpResponse>>

    @POST("otp/generate")
    suspend fun sendOtp(@Body sendOtpRequest: OtpRequest): Response<ApiResponse<Any>>

    @POST("otp/resend")
    suspend fun resendOtp(@Body resendOtpRequest: OtpRequest): Response<ApiResponse<Any>>

    @POST("otp/verify")
    suspend fun verifyOtp(@Body verifyOtpRequest: OtpRequest): Response<ApiResponse<Any>>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<ApiResponse<Any>>

    @GET("/users/{uniqueId}/exists")
    suspend fun verifyCode(@Path("uniqueId") uniqueId: String): Response<ApiResponse<CodeVerificationResponse>>

    @POST("/users/emailId")
    suspend fun verifyUser(@Body verifyUser: VerifyUser): Response<ApiResponse<SignUpResponse>>

    @PATCH("/users/{userId}")
    suspend fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest, @Path("userId") userId: String): Response<SignUpResponse>

    @POST("/auth/{userId}")
    suspend fun refreshToken(@Path("userId") userId: String): Response<ApiResponse<String>>
}