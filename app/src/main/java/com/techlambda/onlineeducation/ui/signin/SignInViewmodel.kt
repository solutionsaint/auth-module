package com.techlambda.onlineeducation.ui.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.repository.auth.AuthRepository
import com.techlambda.onlineeducation.utils.onError
import com.techlambda.onlineeducation.utils.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val authRepository: AuthRepository
): ViewModel() {

    private val _uiStates = MutableStateFlow(SignInUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<SignUpUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()


    fun onEvent(event: SignInUiActions) {
        when (event) {
            is SignInUiActions.NameChanged -> {
                _uiStates.update {
                    it.copy(name = event.name)
                }
            }

            is SignInUiActions.EmailChanged -> {
                _uiStates.value = _uiStates.value.copy(email = event.email)
            }

            is SignInUiActions.NumberChanged -> {
                _uiStates.value = _uiStates.value.copy(number = event.number)
            }

            is SignInUiActions.PasswordChanged -> {
                _uiStates.value = _uiStates.value.copy(password = event.password)
            }

            is SignInUiActions.ConfirmPasswordChanged -> {
                _uiStates.value = _uiStates.value.copy(confirmPassword = event.confirmPassword)
            }

            is SignInUiActions.TogglePasswordVisibility -> {
                _uiStates.value =
                    _uiStates.value.copy(isPasswordVisible = !_uiStates.value.isPasswordVisible)
            }

            is SignInUiActions.SignIn -> {
                signIn()
            }

        }
    }

    private fun signIn() {
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
        viewModelScope.launch {
            _uiStates.update {
                it.copy(isLoading = true)
            }
            authRepository.login(
                LoginRequestModel(
                    username = _uiStates.value.name,
                    password = _uiStates.value.password,
                    email = _uiStates.value.email,
                    role = "",
                )
            ).onSuccess {
                _uiEvents.trySend(SignUpUiEvents.SignInSuccess("Success"))
                _uiStates.update {
                    it.copy(isLoading = false)
                }
            }.onError {
                _uiEvents.trySend(SignUpUiEvents.OnError(it))
                _uiStates.update {
                    it.copy(isLoading = false)

                }
            }
        }
    }


}

data class SignInUiState(
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed class SignInUiActions {
    data class NameChanged(val name: String) : SignInUiActions()
    data class NumberChanged(val number: String) : SignInUiActions()
    data class EmailChanged(val email: String) : SignInUiActions()
    data class PasswordChanged(val password: String) : SignInUiActions()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignInUiActions()
    data object TogglePasswordVisibility : SignInUiActions()
    data object SignIn : SignInUiActions()
}

sealed class SignUpUiEvents {
    data object None : SignUpUiEvents()
    data class SignInSuccess(val message: String) : SignUpUiEvents()
    data class OnError(val message: String) : SignUpUiEvents()
}
