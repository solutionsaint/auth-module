package com.techlambda.onlineeducation.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.techlambda.onlineeducation.Repository.MainRepository
import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignupRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignupResponseModel
import com.techlambda.onlineeducation.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val loginResult: StateFlow<NetworkResult<LoginResponseModel>?>
        get() = repository.loginResponse

    val signUpResult: StateFlow<NetworkResult<SignupResponseModel>?>
        get() = repository.signUpResponse

    fun login(loginRequestModel: LoginRequestModel) {
        viewModelScope.launch {
            repository.login(loginRequestModel)
        }
    }
    fun resetLoginResponse() {
        viewModelScope.launch {
            repository.resetLoginResponse()
        }
    }
    fun signUp(signUpRequestModel: SignupRequestModel) {
        viewModelScope.launch {
            repository.signUp(signUpRequestModel)
        }
    }

    fun resetSignUpResponse() {
        viewModelScope.launch {
            repository.resetSignUpResponse()
        }
    }

    fun validateSignIn(userName: String, password: String): Boolean =
        userName.isEmpty() || password.isEmpty()

    fun validateSignUp(
        userName: String,
        password: String,
        mobileNumber: String,
        email: String,
        confirmPassword: String,
        termsAndCondition: Boolean
    ): String{
        return when {
            userName.isEmpty() -> "Please enter Name"
            mobileNumber.isEmpty() && email.isEmpty() -> "Please provide either a Mobile Number or an Email Address"
            password.isEmpty() -> "Please enter Password"
            confirmPassword.isEmpty() -> "Please enter Confirm Password"
            password != confirmPassword -> "Passwords do not match"
            !termsAndCondition -> "Please accept terms and conditions"
            else -> "Validated"
        }
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberPattern = "^[+]?[0-9]{10,13}\$"
        return Pattern.matches(phoneNumberPattern, phoneNumber)
    }
    fun isPhoneNumber(input: String): Boolean {
        val numericPattern = "^[0-9]+$"
        return input.matches(Regex(numericPattern))
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return Pattern.matches(emailPattern, email)
    }
}