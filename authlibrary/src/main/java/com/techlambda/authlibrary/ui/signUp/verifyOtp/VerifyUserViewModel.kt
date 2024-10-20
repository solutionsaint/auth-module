package com.techlambda.authlibrary.ui.signUp.verifyOtp

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.models.OtpRequest
import com.techlambda.authlibrary.ui.models.SignUpResponse
import com.techlambda.authlibrary.ui.signUp.UserRepository
import com.techlambda.authlibrary.ui.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyUserViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(VerifyUserUiState())
    val state: StateFlow<VerifyUserUiState> = _state.asStateFlow()

    fun onEvent(event: VerifyUserUiEvent) {
        when (event) {

            is VerifyUserUiEvent.VerifyUser -> verifyUser(email = event.email)

            is VerifyUserUiEvent.SendVerifyLink -> sendLink(event.email)
            VerifyUserUiEvent.ClearError -> clearError()
        }
    }

    private fun clearError() {
        _state.update { _state.value.copy(error = null) }
    }

    private fun sendLink(email: String) {
        viewModelScope.launch {
            try {
                val otpResponse = repository.sendOtp(OtpRequest(email = email))
                when (otpResponse) {
                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isOtpSent = false,
                            error = "Otp Sent failed: ${otpResponse.message}"
                        )
                    }

                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            isOtpSent = true,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Otp Send error: ${e.message}")
            }
        }
    }

    private fun verifyUser(email: String) {
        // Add verification logic here
        viewModelScope.launch {
            try {
                val response = repository.verifyUser(email)
                when (response) {
                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isUserVerified = false,
                            error = "Verification failed: ${response.message}"
                        )
                    }

                    is NetworkResult.Success -> {
                        if(response.data?.data?.isUserVerified == true) {
                            _state.value = _state.value.copy(
                                isUserVerified = true,
                                error = null,
                                response = response.data?.data
                            )
                        }else {
                            _state.value = _state.value.copy(
                                isUserVerified = false,
                                error = "User Not Verified: ${response.message}"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isUserVerified = false,
                    error = "Verification error: ${e.message}"
                )
            }
        }
    }
}


data class VerifyUserUiState(
    val isUserVerified: Boolean = false,
    val error: String? = null,
    val isOtpSent: Boolean = false,
    val response: SignUpResponse? = null
)

sealed class VerifyUserUiEvent {
    data class VerifyUser(val email: String) : VerifyUserUiEvent()
    data object ClearError : VerifyUserUiEvent()
    data class SendVerifyLink(val email: String) : VerifyUserUiEvent()
}
