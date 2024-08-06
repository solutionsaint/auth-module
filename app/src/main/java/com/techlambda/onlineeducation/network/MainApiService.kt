package com.techlambda.onlineeducation.network

import com.techlambda.onlineeducation.model.ScreenDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApiService {
    @GET("/Virtual/{screenName}")
    suspend fun getScreenData(@Path("screenName") screenName: String): Response<ScreenDataModel>

}