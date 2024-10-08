package com.techlambda.authlibrary.ui.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.signUp.ResetPasswordRequest
import com.techlambda.authlibrary.ui.signUp.SignInRequest
import com.techlambda.authlibrary.ui.signUp.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiStates = MutableStateFlow(SignInUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<SignUpUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onEvent(event: SignInUiActions) {
        when (event) {
            is SignInUiActions.NameChanged -> {
                _uiStates.update { it.copy(name = event.name) }
            }
            is SignInUiActions.EmailChanged -> {
                _uiStates.update { it.copy(email = event.email) }
            }
            is SignInUiActions.NumberChanged -> {
                _uiStates.update { it.copy(number = event.number) }
            }
            is SignInUiActions.PasswordChanged -> {
                _uiStates.update { it.copy(password = event.password) }
            }
            is SignInUiActions.ConfirmPasswordChanged -> {
                _uiStates.update { it.copy(confirmPassword = event.confirmPassword) }
            }
            is SignInUiActions.TogglePasswordVisibility -> {
                _uiStates.update { it.copy(isPasswordVisible = !_uiStates.value.isPasswordVisible) }
            }
            is SignInUiActions.OtpChanged -> {
                _uiStates.update { it.copy(otp = event.otp) }
            }
            is SignInUiActions.SignIn -> {
                signIn()
            }
            is SignInUiActions.SendOtpForReset -> {
                sendOtpForReset()
            }
            is SignInUiActions.ResetPassword -> {
                resetPassword()
            }
            is SignInUiActions.ClearError -> {
                clearError()
            }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoading = true) }
            try {
                val response = repository.signIn(
                    SignInRequest(
                        email = _uiStates.value.email,
                        password = _uiStates.value.password,
                        type = "email"
                    )
                )
                Log.d("SignInViewModel", "API Response: $response")
                if (response.statusCode == 200) {
                    _uiEvents.send(SignUpUiEvents.SignInSuccess("Sign-in successful!"))
                } else {
                    _uiEvents.send(SignUpUiEvents.OnError("No such user registered"))
                }
            } catch (e: Exception) {
                _uiEvents.send(SignUpUiEvents.OnError("Sign-in error: ${e.message}"))
            } finally {
                _uiStates.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun sendOtpForReset() {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoading = true) }
            try {
                val response = repository.resetPassword(
                    ResetPasswordRequest(
                        email = _uiStates.value.email,
                        username = _uiStates.value.name,
                        type = "send-otp"
                    )
                )
                if (response.success) {
                    _uiStates.update { it.copy(isOtpSent = true) }
                } else {
                    _uiEvents.send(SignUpUiEvents.OnError("Password reset failed: ${response.message}"))
                }
            } catch (e: Exception) {
                _uiEvents.send(SignUpUiEvents.OnError("Password reset error: ${e.message}"))
            } finally {
                _uiStates.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoading = true) }
            try {
                val response = repository.resetPassword(
                    ResetPasswordRequest(
                        email = _uiStates.value.email,
                        type = "verify-otp",
                        username = _uiStates.value.name,
                        otp = _uiStates.value.otp,
                        password = _uiStates.value.password
                    )
                )
                if (response.success) {
                    _uiStates.update { it.copy(isPasswordReset = true) }
                } else {
                    _uiEvents.send(SignUpUiEvents.OnError("Password reset failed: ${response.message}"))
                }
            } catch (e: Exception) {
                _uiEvents.send(SignUpUiEvents.OnError("Password reset error: ${e.message}"))
            } finally {
                _uiStates.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun clearError() {
        viewModelScope.launch {
            _uiEvents.send(SignUpUiEvents.None)
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
    val isLoading: Boolean = false,
    val otp: String = "",
    var isPasswordReset: Boolean = false,
    var isOtpSent: Boolean = false
)

sealed class SignInUiActions {
    data class NameChanged(val name: String) : SignInUiActions()
    data class NumberChanged(val number: String) : SignInUiActions()
    data class EmailChanged(val email: String) : SignInUiActions()
    data class PasswordChanged(val password: String) : SignInUiActions()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignInUiActions()
    data class OtpChanged(val otp: String) : SignInUiActions()
    object TogglePasswordVisibility : SignInUiActions()
    object SignIn : SignInUiActions()
    object ResetPassword : SignInUiActions()
    object SendOtpForReset : SignInUiActions()
    object ClearError : SignInUiActions()
}

sealed class SignUpUiEvents {
    object None : SignUpUiEvents()
    data class SignInSuccess(val message: String) : SignUpUiEvents()
    data class OnError(val message: String) : SignUpUiEvents()
}
