package com.techlambda.onlineeducation.ui.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class SignInViewModel : ViewModel() {

    private val _uiStates = MutableStateFlow(SignInUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<SignInUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()


    fun onEvent(event: SignInUiActions) {
        when (event) {
            is SignInUiActions.EmailChanged -> {
                _uiStates.value = _uiStates.value.copy(email = event.email)
            }

            is SignInUiActions.PasswordChanged -> {
                _uiStates.value = _uiStates.value.copy(password = event.password)
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
        // Add your sign-in logic here
        // You can make network requests using the viewModelScope
        viewModelScope.launch {
            // For example, calling a repository method
            // repository.signIn(_state.value.email, _state.value.password)
        }
    }
}

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed class SignInUiActions {
    data class EmailChanged(val email: String) : SignInUiActions()
    data class PasswordChanged(val password: String) : SignInUiActions()
    data object TogglePasswordVisibility : SignInUiActions()
    data object SignIn : SignInUiActions()
}

sealed class SignInUiEvents {
    data object None : SignInUiEvents()
    data class SignInSuccess(val message: String) : SignInUiEvents()
    data class OnError(val message: String) : SignInUiEvents()
}
