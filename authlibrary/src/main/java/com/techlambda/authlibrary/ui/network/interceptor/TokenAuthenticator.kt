package com.techlambda.authlibrary.ui.network.interceptor

import com.techlambda.authlibrary.ui.data.AuthPrefManager
import com.techlambda.authlibrary.ui.data.TokenManager
import com.techlambda.authlibrary.ui.signUp.ApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authPrefManager: AuthPrefManager,
    private val apiService: Provider<ApiService>
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // Prevent infinite loops by checking the number of prior responses
        if (responseCount(response) >= 3) {
            return null
        }

        // Synchronously refresh the token
        val newTokens = runBlocking {
            val userId = authPrefManager.getUserData()?.userId
            if (userId.isNullOrEmpty()) {
                null
            } else {
                val refreshResponse = apiService.get().refreshToken(userId)
                if (refreshResponse.isSuccessful) {
                    refreshResponse.body()?.data?.let { refreshResponseBody ->
                        tokenManager.saveTokens(
                            refreshResponseBody.accessToken
                        )
                        refreshResponseBody
                    }
                } else {
                    null
                }
            }
        }

        return if (newTokens != null) {
            // Retry the request with the new access token
            response.request.newBuilder()
                .header("Authorization", "Bearer ${newTokens.accessToken}")
                .build()
        } else {
            // Refresh failed, clear tokens and return null
            runBlocking {
                tokenManager.clearTokens()
            }
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }
}