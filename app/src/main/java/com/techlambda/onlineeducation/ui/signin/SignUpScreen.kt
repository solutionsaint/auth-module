package com.techlambda.onlineeducation.ui.signin


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.navigation.LocalNavigationProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: SignInViewModel = hiltViewModel()) {
    val navHostController = LocalNavigationProvider.current
    val uiState = viewModel.state.collectAsStateWithLifecycle().value

    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("") }
    val roles = listOf("Student", "Admin")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.mipmap.app_icon),
                contentDescription = "App Icon",
                Modifier.padding(top = 15.dp)
            )
            Text(text = "Sign Up", modifier = Modifier.padding(top = 20.dp))
            Text(
                text = "Enter your details to register",
                modifier = Modifier.padding(10.dp)
            )
        }
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { viewModel.onEvent(SignInUiActions.NameChanged(it)) },
            label = { Text("Name*") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = {
                viewModel.onEvent(SignInUiActions.EmailChanged(it))
            },
            label = { Text("Email*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
            value = uiState.number,
            onValueChange = { viewModel.onEvent(SignInUiActions.NumberChanged(it)) },
            label = { Text("Phone No:") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Text(text = "Select Your Role", modifier = Modifier.padding(start = 10.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = { selectedRole = it },
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

        val passwordVisibility = uiState.isPasswordVisible // You should manage this state properly
        OutlinedTextField(
            value = uiState.password,
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
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
            supportingText = {

            }
        )
        val ConfirmPasswordVisibility =
            uiState.isPasswordVisible // You should manage this state properly
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = {
                viewModel.onEvent(SignInUiActions.ConfirmPasswordChanged(it))
            },
            label = { Text("Confirm Password*") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.onEvent(SignInUiActions.TogglePasswordVisibility)
                }) {
                    Icon(
                        if (ConfirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
        /*CheckBox(
            checked = termsAndConditionState,
            text = termsAndCondition,
            onTextClicked = {
                navigateToTermsAndConditionScreen()
            }
        ) {
            viewiewModel.updateTermsAndCondition(it)
        }*/
        Button(
            onClick = {
                viewModel.onEvent(SignInUiActions.SignIn)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0XFF05A8B3)
            ), shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            Text(
                text = "Sign In",

                fontWeight = FontWeight(600),
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 18.sp
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun SignupScreenPrev() {
    SignUpScreen()
}