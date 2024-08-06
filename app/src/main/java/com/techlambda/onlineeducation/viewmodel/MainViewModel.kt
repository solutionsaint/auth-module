package com.techlambda.onlineeducation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.onlineeducation.model.ScreenDataModel
import com.techlambda.onlineeducation.network.ApiResult
import com.techlambda.onlineeducation.repository.MainRepository
import com.techlambda.onlineeducation.utils.Constants.aboutUs
import com.techlambda.onlineeducation.utils.Constants.contributorScreen
import com.techlambda.onlineeducation.utils.Constants.homeScreen
import com.techlambda.onlineeducation.utils.Constants.ourServicesScreen
import com.techlambda.onlineeducation.utils.Constants.productPortfolioScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _homeScreenData = MutableStateFlow<ApiResult<ScreenDataModel>>(ApiResult.Loading())
    val homeScreenData: StateFlow<ApiResult<ScreenDataModel>> = _homeScreenData

    private val _aboutUsScreenData = MutableStateFlow<ApiResult<ScreenDataModel>>(ApiResult.Loading())
    val aboutUsScreenData: StateFlow<ApiResult<ScreenDataModel>> = _aboutUsScreenData

    private val _servicesScreenData = MutableStateFlow<ApiResult<ScreenDataModel>>(ApiResult.Loading())
    val servicesScreenData: StateFlow<ApiResult<ScreenDataModel>> = _servicesScreenData

    private val _productsScreenData = MutableStateFlow<ApiResult<ScreenDataModel>>(ApiResult.Loading())
    val productsScreenData: StateFlow<ApiResult<ScreenDataModel>> = _productsScreenData

    private val _contributorsScreenData = MutableStateFlow<ApiResult<ScreenDataModel>>(ApiResult.Loading())
    val contributorsScreenData: StateFlow<ApiResult<ScreenDataModel>> = _contributorsScreenData

    fun getHomeScreenData(){
        viewModelScope.launch {
            _homeScreenData.value = repository.getScreenData(homeScreen)
        }
    }

    fun getAboutUsScreenData(){
        viewModelScope.launch {
            _aboutUsScreenData.value = repository.getScreenData(aboutUs)
        }
    }

    fun getServicesScreenData(){
        viewModelScope.launch {
            _servicesScreenData.value = repository.getScreenData(ourServicesScreen)
        }
    }

    fun getProductsScreenData(){
        viewModelScope.launch {
            _productsScreenData.value = repository.getScreenData(productPortfolioScreen)
        }
    }

    fun getContributorScreenData(){
        viewModelScope.launch {
            _contributorsScreenData.value = repository.getScreenData(contributorScreen)
        }
    }
}