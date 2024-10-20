package com.techlambda.authlibrary.ui.network.interceptor

import com.techlambda.authlibrary.ui.data.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking {
            tokenManager.accessToken.firstOrNull()
        }

        val newRequest = originalRequest.newBuilder()
            .apply {
                if (!token.isNullOrEmpty()) {
                    header("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(newRequest)
    }
}