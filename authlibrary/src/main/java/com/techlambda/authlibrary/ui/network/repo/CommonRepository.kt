package com.techlambda.authlibrary.ui.network.repo

import com.techlambda.authlibrary.ui.models.ApiResponse
import com.techlambda.authlibrary.ui.models.FilterRequest
import com.techlambda.authlibrary.ui.models.FilterResponse
import com.techlambda.authlibrary.ui.signUp.ApiService
import com.techlambda.authlibrary.ui.utils.NetworkResult
import com.techlambda.authlibrary.ui.utils.makeApiCall
import javax.inject.Inject

class CommonRepository @Inject constructor(private val api: ApiService) {

    suspend fun masterFilter(filterRequest: FilterRequest): NetworkResult<ApiResponse<List<FilterResponse>>> {
        return makeApiCall({ api.masterFilter(filterRequest) }, filterRequest)
    }
}