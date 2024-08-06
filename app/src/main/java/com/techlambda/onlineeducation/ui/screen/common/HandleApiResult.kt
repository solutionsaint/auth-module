package com.techlambda.onlineeducation.ui.screen.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techlambda.onlineeducation.network.ApiResult

@Composable
fun <T> HandleApiResult(
    padding: PaddingValues,
    apiResult: ApiResult<T>,
    content: @Composable (T?) -> Unit
) {
    when (apiResult) {
        is ApiResult.Loading -> {
            ShowLoadingScreen()
        }
        is ApiResult.Success -> {
            val data = apiResult.data
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
            ) {
                content(data)
            }
        }
        is ApiResult.Error -> {
            val message = apiResult.message
            NoDataView(message = message)
        }
    }
}