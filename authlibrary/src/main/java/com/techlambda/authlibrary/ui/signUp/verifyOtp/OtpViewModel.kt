package com.techlambda.authlibrary.ui.signUp.verifyOtp

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.techlambda.authlibrary.ui.signUp.ApiService
import com.techlambda.authlibrary.ui.signUp.OtpRequest
import com.techlambda.authlibrary.ui.signUp.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: UserRepository,
    private val apiService: ApiService
) : ViewModel() {

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
                resendOtp(_state.value.otpSentTo ?: "")
            }
        }
    }

    private fun verifyOtp() {
        // Add verification logic here
        viewModelScope.launch {
            try {
                val response = apiService.verifyOtp(
                    OtpRequest(otp = _state.value.otp)
                )
                if (response.success) {
                    Log.d("OtpViewModel", "OTP verification successful")
                    _state.value = _state.value.copy(isOtpVerified = true, error = null)
                } else {
                    _state.value = _state.value.copy(isOtpVerified = false, error = "Verification failed: ${response.message}")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isOtpVerified = false, error = "Verification error: ${e.message}")
            }
        }
    }

    private fun resendOtp(email: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                otp = "",
                isResendButtonVisible = false,
                timer = 30
            )
            startTimer()
            try {
                val response = apiService.resendOtp(
                    OtpRequest(email=email)  // Adjust according to your actual request needs
                )
                if (response.success) {
                    _state.value = _state.value.copy(isOtpSent = true, error = null)
                } else {
                    _state.value = _state.value.copy(isOtpSent = false, error = "Resend failed: ${response.message}")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isOtpSent = false, error = "Resend error: ${e.message}")
            }
        }
    }
    fun generateQrCode(appName: String = "com.android.chrome", callback: (Bitmap?) -> Unit) {
        val url = "https://play.google.com/store/apps/details?id=$appName"
        val bitmap = createQrCodeBitmap(url)
        callback(bitmap)
    }

    private fun createQrCodeBitmap(url: String): Bitmap? {
        val size = 512 // Size of the QR code
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}



data class OtpUiState(
    val otp: String = "",
    val timer: Int = 30,
    val isResendButtonVisible: Boolean = false,
    val isOtpSent: Boolean = false,
    var otpSentTo: String? = null,
    val isOtpVerified: Boolean = false,
    val error: String? = null
)

sealed class OtpUiEvent {
    data class OtpChanged(val otp: String) : OtpUiEvent()
    data object VerifyOtp : OtpUiEvent()
    data object ResendOtp : OtpUiEvent()
}
