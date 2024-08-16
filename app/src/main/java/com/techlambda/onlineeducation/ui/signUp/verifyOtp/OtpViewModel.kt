package com.techlambda.onlineeducation.ui.signUp.verifyOtp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(OtpUiState())
    val state: StateFlow<OtpUiState> = _state.asStateFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            for (i in 30 downTo 0) {
                _state.value = _state.value.copy(timer = i)
                delay(1000L)
            }
            _state.value = _state.value.copy(isResendButtonVisible = true)
        }
    }

    fun onEvent(event: OtpUiEvent) {
        when (event) {
            is OtpUiEvent.OtpChanged -> {
                _state.value = _state.value.copy(otp = event.otp)
            }
            is OtpUiEvent.VerifyOtp -> {
                verifyOtp()
            }
            is OtpUiEvent.ResendOtp -> {
                resendOtp()
            }
        }
    }

    private fun verifyOtp() {
        // Add verification logic here
    }

    private fun resendOtp() {
        // Reset the state and start the timer again
        _state.value = _state.value.copy(
            otp = "",
            isResendButtonVisible = false,
            timer = 30
        )
        startTimer()
        // Add resend logic here
    }
}

data class OtpUiState(
    val otp: String = "",
    val timer: Int = 30,
    val isResendButtonVisible: Boolean = false
)

sealed class OtpUiEvent {
    data class OtpChanged(val otp: String) : OtpUiEvent()
    data object VerifyOtp : OtpUiEvent()
    data object ResendOtp : OtpUiEvent()
}
