package com.techlambda.authlibrary.ui.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.techlambda.authlibrary.navigation.AppNavigation

@Composable
fun ResetPasswordScreen(
    onPasswordReset: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    state.isPasswordReset =false
    var otpSent by remember{
        mutableStateOf(false)
    }

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
                    navHostController.navigate(AppNavigation.SignInScreen)
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
