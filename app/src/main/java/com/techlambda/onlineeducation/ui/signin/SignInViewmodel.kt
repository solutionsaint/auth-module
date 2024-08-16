package com.techlambda.onlineeducation.ui.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
) : ViewModel() {

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
