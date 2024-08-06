package com.techlambda.onlineeducation.repository

import com.techlambda.onlineeducation.model.ScreenDataModel
import com.techlambda.onlineeducation.network.MainApiService
import com.techlambda.onlineeducation.network.ApiResult
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainAPIService: MainApiService) {
    suspend fun getScreenData(screenName: String): ApiResult<ScreenDataModel> {
        return makeApiCall({mainAPIService.getScreenData(screenName)}, screenName)
    }
}

suspend fun <Req, Res> makeApiCall( apiCall: suspend (Req) -> Response<Res>,request: Req ): ApiResult<Res> {
    return try {
        val response = apiCall(request)
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error("Empty response body")
        } else {
            ApiResult.Error("API call failed with error: ${response.errorBody()?.string()}")
        }
    } catch (e: IOException) {
        ApiResult.Error("Network error: ${e.message}")
    } catch (e: Exception) {
        ApiResult.Error("Unexpected error: ${e.message}")
    }
}