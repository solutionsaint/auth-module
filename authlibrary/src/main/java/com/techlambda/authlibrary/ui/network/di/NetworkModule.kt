package com.techlambda.authlibrary.ui.network.di

import com.techlambda.authlibrary.ui.data.AuthPrefManager
import com.techlambda.authlibrary.ui.data.TokenManager
import com.techlambda.authlibrary.ui.network.interceptor.AuthInterceptor
import com.techlambda.authlibrary.ui.network.interceptor.TokenAuthenticator
import com.techlambda.authlibrary.ui.signUp.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        authInterceptor: AuthInterceptor,
        authPrefManager: AuthPrefManager,
        tokenManager: TokenManager,
        apiServiceProvider: Provider<ApiService>
    ): Retrofit {

        val authenticator =
            TokenAuthenticator(tokenManager, authPrefManager = authPrefManager, apiServiceProvider)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Create OkHttpClient and add the logging interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://techlambda.com:9002/")  // Replace with your actual base URL
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