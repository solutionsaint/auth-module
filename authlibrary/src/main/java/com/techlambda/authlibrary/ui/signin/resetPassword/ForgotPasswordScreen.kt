package com.techlambda.authlibrary.ui.signin.resetPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techlambda.authlibrary.ui.utils.isValidEmail
import com.techlambda.common.utils.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    verifyEmail: (email: String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    Scaffold (
        topBar =  { TopAppBar(title = { Text("Forgot Password", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) } ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email field to enter email before sending OTP
            TextField(
                value = email,
                onValueChange = { email = it.trim() },
                label = { Text(text = "Enter Email") },
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    if (email.isNotEmpty() && isValidEmail(email)) {
                        verifyEmail(email)
                    } else if(email.isEmpty()){
                        context.showToast("Email is required.")
                    } else {
                        context.showToast("Email is invalid.")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            ) {
                Text(text = "Verify")
            }
        }
    }
}