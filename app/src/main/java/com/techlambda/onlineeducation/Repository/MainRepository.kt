package com.techlambda.onlineeducation.Repository

import MainApiService
import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignupRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignupResponseModel
import com.techlambda.onlineeducation.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainApiService: MainApiService) {

    private val _loginResponse = MutableStateFlow<NetworkResult<LoginResponseModel>?>(null)
    val loginResponse: StateFlow<NetworkResult<LoginResponseModel>?>
        get() = _loginResponse

    private val _signUpResponse = MutableStateFlow<NetworkResult<SignupResponseModel>?>(null)
    val signUpResponse: StateFlow<NetworkResult<SignupResponseModel>?>
        get() = _signUpResponse


    suspend fun login(loginRequestModel: LoginRequestModel) {
        _loginResponse.emit(NetworkResult.Loading())
        try {
            val result = mainApiService.login(loginRequestModel)
            if (result.body()?.status == 200 && result.body() != null) {
                _loginResponse.emit(NetworkResult.Success(result.body()!!))
            } else {
                if(result.errorBody() != null){
                    val errorObj = JSONObject(result.errorBody()!!.charStream().readText())
                    _loginResponse.emit(NetworkResult.Error(errorObj.getString("message")))
                }else {
                    _loginResponse.emit(NetworkResult.Error(result.body()?.message))
                }
            }
        } catch (e: Exception) {
            _loginResponse.emit(NetworkResult.Error(e.message))
        }
    }
    suspend fun resetLoginResponse() {
        _loginResponse.emit(null)
    }

    suspend fun signUp(signUpRequestModel: SignupRequestModel) {
        if (signUpRequestModel.email.isNullOrEmpty() && signUpRequestModel.mobileNumber.isNullOrEmpty()) {
            _signUpResponse.emit(NetworkResult.Error("Please provide either an email address or a phone number."))
            return
        }
        _signUpResponse.emit(NetworkResult.Loading())
        try {
            val result = mainApiService.signUp(signUpRequestModel)
            if (result.body() != null && result.body()?.status == 200) {
            }else{
                    if(result.errorBody() != null){
                        val errorObj = JSONObject(result.errorBody()!!.charStream().readText())
                        _signUpResponse.emit(NetworkResult.Error(errorObj.getString("message")))
                    }else {
                        _signUpResponse.emit(NetworkResult.Error(result.body()?.message))
                    }
                }

        } catch (e: Exception) {
            _signUpResponse.emit(NetworkResult.Error(e.message))
        }
    }
    suspend fun resetSignUpResponse() {
        _signUpResponse.emit(null)
    }
}
