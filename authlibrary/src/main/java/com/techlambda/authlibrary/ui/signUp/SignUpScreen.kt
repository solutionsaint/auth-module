package com.techlambda.authlibrary.ui.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlambda.authlibrary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    appLogo: @Composable BoxScope.() -> Unit,
    onSignUpSuccess: (email: String, id: String) -> Unit,
    onSignInClick: () -> Unit
) {

    val uiState = viewModel.state.collectAsStateWithLifecycle().value
    val uiEvents = viewModel.uiEvents.collectAsStateWithLifecycle(SignUpUiEvents.None).value

    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("") }
    val roles = listOf("User", "Admin")
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    LaunchedEffect(selectedRole) {
        viewModel.onEvent(SignUpUiActions.UserTypeChanged(selectedRole))
    }

    LaunchedEffect(key1 = uiEvents) {
        when (uiEvents) {
            SignUpUiEvents.None -> {

            }

            is SignUpUiEvents.OnError -> {
                errorMessage = uiEvents.message
                showErrorDialog = true
            }

            is SignUpUiEvents.SignUpSuccess -> {
                onSignUpSuccess(uiState.email, uiEvents.message)
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Signup Error") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(onClick = {
                    showErrorDialog = false
                }) {
                    Text("Try Again")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp)
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                appLogo()
            }
            Text(text = "Sign Up", modifier = Modifier.padding(top = 20.dp))
            Text(
                text = "Enter your details to register",
                modifier = Modifier.padding(10.dp)
            )
        }
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { viewModel.onEvent(SignUpUiActions.NameChanged(it)) },
            label = { Text("Name*") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = {
                viewModel.onEvent(SignUpUiActions.EmailChanged(it))
            },
            label = { Text("Email*") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = uiState.number,
            onValueChange = { viewModel.onEvent(SignUpUiActions.NumberChanged(it)) },
            label = { Text("Phone No:") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Text(
            text = "Select Your Role",
            modifier = Modifier.padding(top = 20.dp),
            fontWeight = FontWeight.Bold,
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {
                    selectedRole = it
                },
                readOnly = true,
                label = { Text("Please Select Your Role") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        onClick = {
                            selectedRole = role
                            expanded = false
                        },
                        text = { Text(text = role) }
                    )
                }
            }
        }

        var passwordVisibility by remember {
            mutableStateOf(false)
        }
        OutlinedTextField(
            value = uiState.password,
            onValueChange = {
                viewModel.onEvent(SignUpUiActions.PasswordChanged(it))
            },
            label = { Text("Password*") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
            supportingText = {

            }
        )
        var confirmPasswordVisibility by remember {
            mutableStateOf(false)
        }
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = {
                viewModel.onEvent(SignUpUiActions.ConfirmPasswordChanged(it))
            },
            label = { Text("Confirm Password*") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    confirmPasswordVisibility = !confirmPasswordVisibility
                }) {
                    Icon(
                        if (confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
            supportingText = {

            }
        )
        if (uiState.confirmPassword.isNotEmpty()) {
            Row {
                Image(
                    imageVector = if (uiState.password == uiState.confirmPassword) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = "Icon",
                    colorFilter = ColorFilter.tint(
                        if (uiState.password != uiState.confirmPassword) Color(
                            0xFFB9533E
                        ) else Color(0xFF42B93E)
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                )
                Text(
                    text = if (uiState.password != uiState.confirmPassword) "Passwords Do not match" else "Passwords Match",
                    color = if (uiState.password != uiState.confirmPassword) Color(0xFFB9533E) else Color(
                        0xFF42B93E
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )

            }
        }

        Button(
            onClick = {
                viewModel.onEvent(SignUpUiActions.SignUp)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            Text(
                text = "Sign Up",
                fontWeight = FontWeight(600),
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have an account?", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "   Sign In",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onSignInClick()
                }
            )
        }
    }
}