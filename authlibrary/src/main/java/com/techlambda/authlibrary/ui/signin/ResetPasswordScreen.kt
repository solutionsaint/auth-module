package com.techlambda.authlibrary.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ResetPasswordScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onPasswordReset: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    state.isPasswordReset = false
    var otpSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email field to enter email before sending OTP
        TextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(SignInUiActions.EmailChanged(it)) },
            label = { Text(text = "Enter Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(SignInUiActions.NameChanged(it)) },
            label = { Text(text = "Enter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to send OTP
        Button(
            onClick = {
                viewModel.onEvent(SignInUiActions.SendOtpForReset) // New action to handle OTP sending
                otpSent = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Send OTP")
        }

        // Show OTP and Password fields only after OTP is sent
        if (otpSent) {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = state.otp,
                onValueChange = { viewModel.onEvent(SignInUiActions.OtpChanged(it)) },
                label = { Text(text = "Enter OTP") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(SignInUiActions.PasswordChanged(it)) },
                label = { Text("New Password") },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onEvent(SignInUiActions.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onEvent(SignInUiActions.ConfirmPasswordChanged(it)) },
                label = { Text("Re-enter New Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onEvent(SignInUiActions.ResetPassword)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Reset Password")
            }
        }
    }

    LaunchedEffect(state.isPasswordReset) {
        if (state.isPasswordReset) {
            onPasswordReset()
        }
    }
}

@Preview
@Composable
fun ResetPasswordPrev(modifier: Modifier = Modifier) {
    ResetPasswordScreen {

    }
}