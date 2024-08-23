package com.techlambda.onlineeducation.ui.signUp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

interface ApiService {

    @POST("auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): ApiResponse<Any>

    @POST("auth/signin")
    suspend fun signIn(@Body signInRequest: SignInRequest): ApiResponse<Any>

    @POST("otp/send")
    suspend fun sendOtp(@Body sendOtpRequest: OtpRequest): ApiResponse<Any>

    @POST("otp/resend")
    suspend fun resendOtp(@Body resendOtpRequest: OtpRequest): ApiResponse<Any>

    @POST("otp/verify")
    suspend fun verifyOtp(@Body verifyOtpRequest: OtpRequest): ApiResponse<Any>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): ApiResponse<Any>
}

@Module
@InstallIn(SingletonComponent::class)  // Ensure it's available across the app
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://techlambda.com:9000/")  // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}