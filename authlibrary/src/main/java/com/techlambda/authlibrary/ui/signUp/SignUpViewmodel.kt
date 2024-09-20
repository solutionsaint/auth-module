package com.techlambda.authlibrary.ui.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.models.OtpRequest
import com.techlambda.authlibrary.ui.models.SignUpRequest
import com.techlambda.authlibrary.ui.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiStates = MutableStateFlow(SignUpUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<SignUpUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()


    fun onEvent(event: SignUpUiActions) {
        when (event) {
            is SignUpUiActions.NameChanged -> {
                _uiStates.update {
                    it.copy(name = event.name)
                }
            }

            is SignUpUiActions.EmailChanged -> {
                _uiStates.value = _uiStates.value.copy(email = event.email)
            }

            is SignUpUiActions.NumberChanged -> {
                _uiStates.value = _uiStates.value.copy(number = event.number)
            }

            is SignUpUiActions.PasswordChanged -> {
                _uiStates.value = _uiStates.value.copy(password = event.password)
            }

            is SignUpUiActions.ConfirmPasswordChanged -> {
                _uiStates.value = _uiStates.value.copy(confirmPassword = event.confirmPassword)
            }

            is SignUpUiActions.UserTypeChanged -> {
                _uiStates.value = _uiStates.value.copy(userType = event.userType)
            }

            is SignUpUiActions.SignUp -> {
                signUp()
            }
        }
    }


    private fun signUp() {
        viewModelScope.launch {
            val validationMessage = validateSignUp(
                userName = _uiStates.value.name,
                password = _uiStates.value.password,
                mobileNumber = _uiStates.value.number,
                email = _uiStates.value.email,
                confirmPassword = _uiStates.value.confirmPassword,
                termsAndCondition = true // Assuming terms and conditions are accepted for simplicity
            )

            if (validationMessage == "Validated") {
                val req = SignUpRequest(
                    name = _uiStates.value.name,
                    phone = _uiStates.value.number,
                    email = _uiStates.value.email,
                    password = _uiStates.value.password,
                    userType = _uiStates.value.userType
                )
                Log.d("TAG", "signUp: $req")
                val response = repository.signUp(
                   req
                )

                when (response) {
                    is NetworkResult.Error -> {
                        _uiEvents.send(SignUpUiEvents.OnError("An error occurred during signup: ${response.message}"))
                    }

                    is NetworkResult.Success -> {
                        _uiEvents.send(SignUpUiEvents.SignUpSuccess(response.data?.data?.id ?: ""))
                    }
                }
            } else {
                viewModelScope.launch {
                    _uiEvents.send(SignUpUiEvents.OnError(validationMessage))
                }
            }
        }
    }

    fun validateSignUp(
        userName: String,
        password: String,
        mobileNumber: String,
        email: String,
        confirmPassword: String,
        termsAndCondition: Boolean
    ): String {
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

data class SignUpUiState(
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userType: String = ""
)

sealed class SignUpUiActions {
    data class NameChanged(val name: String) : SignUpUiActions()
    data class NumberChanged(val number: String) : SignUpUiActions()
    data class EmailChanged(val email: String) : SignUpUiActions()
    data class PasswordChanged(val password: String) : SignUpUiActions()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpUiActions()
    data object SignUp : SignUpUiActions()
    data class UserTypeChanged(val userType: String) : SignUpUiActions()
}

sealed class SignUpUiEvents {
    data object None : SignUpUiEvents()
    data class SignUpSuccess(val message: String) : SignUpUiEvents()
    data class OnError(val message: String) : SignUpUiEvents()
}
