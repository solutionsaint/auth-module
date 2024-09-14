package com.techlambda.authlibrary.ui.signUp

import com.techlambda.authlibrary.ui.models.ApiResponse
import com.techlambda.authlibrary.ui.models.CodeVerificationResponse
import com.techlambda.authlibrary.ui.models.OtpRequest
import com.techlambda.authlibrary.ui.models.ResetPasswordRequest
import com.techlambda.authlibrary.ui.models.SignInRequest
import com.techlambda.authlibrary.ui.models.SignUpRequest
import com.techlambda.authlibrary.ui.models.SignUpResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Singleton

interface ApiService {

    @POST("auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<ApiResponse<SignUpResponse>>

    @POST("auth/signin")
    suspend fun signIn(@Body signInRequest: SignInRequest):  Response<ApiResponse<SignUpResponse>>

    @POST("otp/send")
    suspend fun sendOtp(@Body sendOtpRequest: OtpRequest): Response<ApiResponse<Any>>

    @POST("otp/resend")
    suspend fun resendOtp(@Body resendOtpRequest: OtpRequest): Response<ApiResponse<Any>>

    @POST("otp/verify")
    suspend fun verifyOtp(@Body verifyOtpRequest: OtpRequest): Response<ApiResponse<Any>>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<ApiResponse<Any>>

    @GET("/users/{uniqueId}/exists")
    suspend fun verifyCode(@Path("uniqueId") uniqueId: String): Response<ApiResponse<CodeVerificationResponse>>
}

@Module
@InstallIn(SingletonComponent::class)  // Ensure it's available across the app
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Create OkHttpClient and add the logging interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://techlambda.com:9001/")  // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}