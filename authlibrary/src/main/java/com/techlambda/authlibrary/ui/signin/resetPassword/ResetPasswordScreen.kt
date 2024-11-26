package com.techlambda.authlibrary.ui.signin.resetPassword

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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlambda.authlibrary.ui.signin.SignInUiActions
import com.techlambda.authlibrary.ui.signin.SignInViewModel
import com.techlambda.authlibrary.ui.signin.SignUpUiEvents
import com.techlambda.common.utils.showToast

@Composable
fun ResetPasswordScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    email: String,
    onPasswordReset: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val uiEvents = viewModel.uiEvents.collectAsStateWithLifecycle(SignUpUiEvents.None).value
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.onEvent(SignInUiActions.EmailChanged(email))
    }
    when (uiEvents) {
        is SignUpUiEvents.OnError -> {
            errorMessage = uiEvents.message
            showErrorDialog = true
        }

        else -> {}
    }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Reset Password Error") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(onClick = {
                    showErrorDialog = false
                    viewModel.onEvent(SignInUiActions.ClearError)
                }) {
                    Text("Try Again")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(SignInUiActions.PasswordChanged(it.trim())) },
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
            onValueChange = { viewModel.onEvent(SignInUiActions.ConfirmPasswordChanged(it.trim())) },
            label = { Text("Re-enter New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        Button(
            onClick = {
                if (state.password != state.confirmPassword){
                    context.showToast("Password does not match")
                }else {
                    viewModel.onEvent(SignInUiActions.ResetPassword)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset Password")
        }
    }

    LaunchedEffect(state.isPasswordReset) {
        if (state.isPasswordReset) {
            onPasswordReset()
        }
    }
}