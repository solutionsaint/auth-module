package com.techlambda.onlineeducation.data.network

import com.techlambda.onlineeducation.model.ScreenDataModel

interface MainApiService {
    suspend fun getScreenData(screenName: String): ApiResponse<ScreenDataModel>
}