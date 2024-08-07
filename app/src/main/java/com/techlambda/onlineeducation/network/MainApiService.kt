package com.techlambda.onlineeducation.network

import com.techlambda.onlineeducation.model.ScreenDataModel

interface MainApiService {
    suspend fun getScreenData(screenName: String): ApiResponse<ScreenDataModel>
}