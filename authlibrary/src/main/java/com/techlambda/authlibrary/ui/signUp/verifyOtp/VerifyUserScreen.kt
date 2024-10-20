package com.techlambda.authlibrary.ui.signUp.verifyOtp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.techlambda.authlibrary.ui.models.SignUpResponse

@Composable
fun VerifyUserScreen(
    emailId: String,
    navHostController: NavHostController,
    viewModel: VerifyUserViewModel = hiltViewModel(),
    onUserVerified: (response: SignUpResponse?) -> Unit
) {

    val state by viewModel.state.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.onEvent(VerifyUserUiEvent.SendVerifyLink(email = emailId))
    }

    LaunchedEffect(state) {
        if (state.isUserVerified) {
            onUserVerified(state.response)
        }
    }
    val context = LocalContext.current
    if(!state.isUserVerified && state.error != null) {
        viewModel.onEvent(event = VerifyUserUiEvent.ClearError)
        Toast.makeText(context, "Please verify the email and try again.", Toast.LENGTH_SHORT).show()
    }
    if (!state.isOtpSent && state.error != null) {
        showErrorDialog = true
        errorMessage = state.error ?: "Something went wrong..."
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                navHostController.popBackStack()
            },
            title = { Text(text = "Error") },
            text = { Text(text = errorMessage ?: "") },
            confirmButton = {
                Button(onClick = {
                    showErrorDialog = false
                    navHostController.popBackStack()
                }) {
                    Text("Try Again")
                }
            }
        )
    }
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        Text(
            "Verify",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            "We have sent an verification link to $emailId.\nCheck your email for verification link and verify your account then, click on refresh button.",
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp)
        )
        Button(
            onClick = {
                viewModel.onEvent(VerifyUserUiEvent.VerifyUser(emailId))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 100.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Refresh")
        }
    }
}