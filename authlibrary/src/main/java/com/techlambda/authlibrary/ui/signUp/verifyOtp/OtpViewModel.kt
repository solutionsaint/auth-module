package com.techlambda.authlibrary.ui.signUp.verifyOtp

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.techlambda.authlibrary.ui.models.OtpRequest
import com.techlambda.authlibrary.ui.signUp.UserRepository
import com.techlambda.authlibrary.ui.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: UserRepository,
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
                verifyOtp(id = event.id)
            }

            is OtpUiEvent.ResendOtp -> {
                resendOtp(id = event.id)
            }

            is OtpUiEvent.SendOtp -> {
                sendOtp(event.id)
            }
        }
    }

    private fun verifyOtp(id: String) {
        // Add verification logic here
        viewModelScope.launch {
            try {
                val response = repository.verifyOtp(
                    OtpRequest(otp = _state.value.otp, _id = id)
                )
                when (response) {
                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isOtpVerified = false,
                            error = "Verification failed: ${response.message}"
                        )
                    }

                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(isOtpVerified = true, error = null)
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isOtpVerified = false,
                    error = "Verification error: ${e.message}"
                )
            }
        }
    }

    private fun resendOtp(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                otp = "",
                isResendButtonVisible = false,
                timer = 30
            )
            startTimer()
            try {
                val response = repository.resendOtp(
                    OtpRequest(_id = id)  // Adjust according to your actual request needs
                )
                when (response) {
                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isOtpSent = false,
                            error = "Resend failed: ${response.message}"
                        )
                    }

                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(isOtpSent = true, error = null)
                    }
                }
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(isOtpSent = false, error = "Resend error: ${e.message}")
            }
        }
    }

    private fun sendOtp(id: String) {
        viewModelScope.launch {
            try {
                val otpResponse = repository.sendOtp(OtpRequest(_id = id))
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
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix.get(
                                x,
                                y
                            )
                        ) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                    )
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
    val isOtpVerified: Boolean = false,
    val error: String? = null
)

sealed class OtpUiEvent {
    data class OtpChanged(val otp: String) : OtpUiEvent()
    data class VerifyOtp(val id: String) : OtpUiEvent()
    data class ResendOtp(val id: String) : OtpUiEvent()
    data class SendOtp(val id: String) : OtpUiEvent()
}
