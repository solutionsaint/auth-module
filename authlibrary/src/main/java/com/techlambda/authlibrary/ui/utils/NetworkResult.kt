package com.techlambda.authlibrary.ui.utils

import retrofit2.Response
import java.io.IOException

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
}

suspend fun <Req, Res> makeApiCall(apiCall: suspend (Req) -> Response<Res>, request: Req ): NetworkResult<Res> {
    return try {
        val response = apiCall(request)
        if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error("Empty response body")
        } else {
            NetworkResult.Error("API call failed with error: ${response.errorBody()?.string()}")
        }
    } catch (e: IOException) {
        NetworkResult.Error("Network error: ${e.message}")
    } catch (e: Exception) {
        NetworkResult.Error("Unexpected error: ${e.message}")
    }
}