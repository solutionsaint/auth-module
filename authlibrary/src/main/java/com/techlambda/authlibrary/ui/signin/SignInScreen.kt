package com.techlambda.authlibrary.ui.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlambda.authlibrary.navigation.AppNavigation
import com.techlambda.authlibrary.navigation.LocalNavigationProvider
import com.techlambda.authlibrary.ui.signin.SignInUiActions
import com.techlambda.authlibrary.ui.signin.SignInViewModel
import com.techlambda.authlibrary.ui.signin.SignUpUiEvents

@Composable
fun SignInScreen(viewModel: SignInViewModel = hiltViewModel(),
                 onSignInSuccess: () -> Unit) {
    val navHostController = LocalNavigationProvider.current
    val uiStates = viewModel.state.collectAsStateWithLifecycle().value
    val uiEvents = viewModel.uiEvents.collectAsStateWithLifecycle(SignUpUiEvents.None).value
    val context = LocalContext.current

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    when (uiEvents) {
        is SignUpUiEvents.OnError -> {
            errorMessage = uiEvents.message
            showErrorDialog = true
        }

        is SignUpUiEvents.SignInSuccess -> {
            navHostController.navigate(AppNavigation.Home)
        }

        else -> {}
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Login Error") },
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Profile Icon Placeholder
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Online App Logo", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email/Phone Number Input
        OutlinedTextField(
            value = uiStates.email,
            onValueChange = {
                viewModel.onEvent(SignInUiActions.EmailChanged(it))
            },
            label = { Text("Email*") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        val passwordVisibility = uiStates.isPasswordVisible
        OutlinedTextField(
            value = uiStates.password,
            onValueChange = {
                viewModel.onEvent(SignInUiActions.PasswordChanged(it))
            },
            label = { Text("Password*") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.onEvent(SignInUiActions.TogglePasswordVisibility)
                }) {
                    Icon(
                        if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Forgot Password?",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    navHostController.navigate(AppNavigation.ResetPasswordScreen)
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign In Button
        Button(
            onClick = {
                viewModel.onEvent(SignInUiActions.SignIn)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account?", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "   Sign Up",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navHostController.navigate(AppNavigation.SignUpScreen)
                }
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun SignupScreenPrev() {
//    CompositionLocalProvider(value = LocalNavigationProvider provides rememberNavController()) {
//        SignInScreen(
//            viewModel = SignInViewModel()
//        )
//    }
//}
