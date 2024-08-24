package com.techlambda.authlibrary.ui.signUp.verifyOtp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techlambda.authlibrary.navigation.AppNavigation
import com.techlambda.authlibrary.navigation.LocalNavigationProvider
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpInputField
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpUiEvent
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpViewModel

@Composable
fun OtpScreen(viewModel: OtpViewModel = hiltViewModel(), email: String) {
    val navigation = LocalNavigationProvider.current
    val state by viewModel.state.collectAsState()
    var isVerifyButtonEnabled by remember {
        mutableStateOf(false)
    }

    state.otpSentTo = email

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Verify Code", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "An OTP has been sent to $email")

        Spacer(modifier = Modifier.height(16.dp))

        OtpInputField(otpText = state.otp) { otp, isComplete ->
            viewModel.onEvent(OtpUiEvent.OtpChanged(otp))
            isVerifyButtonEnabled = isComplete
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onEvent(OtpUiEvent.VerifyOtp)
                navigation.navigate(AppNavigation.Home::class.toString())
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isVerifyButtonEnabled
        ) {
            Text(text = "Verify")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.timer > 0) {
            Text(text = "Resend OTP in: ${state.timer}")
        } else if (state.isResendButtonVisible) {
            TextButton(
                onClick = { viewModel.onEvent(OtpUiEvent.ResendOtp) }
            ) {
                Text(text = "Re-Send OTP", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}