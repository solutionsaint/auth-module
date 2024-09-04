package com.techlambda.authlibrary.ui.code

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlambda.authlibrary.ui.utils.LoaderDialog
import com.techlambda.authlibrary.ui.utils.LoaderManager

@Composable
fun CodeScreen(
    viewModel: CodeViewModel = hiltViewModel(),
    onCodeSuccess: (String) -> Unit,
    navigateToSignInScreen: () -> Unit,
    appLogo: @Composable ColumnScope.() -> Unit,
    headerText: String
) {
    //as
    val uiStates = viewModel.state.collectAsStateWithLifecycle().value
    val uiEvents = viewModel.uiEvents.collectAsStateWithLifecycle(CodeScreenUiEvents.None).value
    val isLoading by LoaderManager.isLoading

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    when (uiEvents) {
        is CodeScreenUiEvents.OnError -> {
            errorMessage = uiEvents.message
            showErrorDialog = true
        }

        is CodeScreenUiEvents.CodeValidationSuccess -> {
            LaunchedEffect(Unit) {
                Log.d("TAG", "CodeScreen: Success")
                if(uiEvents.codeVerificationResponse.exists) {
                    onCodeSuccess(uiStates.code)
                }else {
                    showErrorDialog = true
                    errorMessage = "Code does not exist."
                }
            }
        }

        else -> {}
    }

    LoaderDialog(isLoading = isLoading)



    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Login Error") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(onClick = {
                    showErrorDialog = false
                    viewModel.onEvent(CodeScreenUiActions.ClearError)
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
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            appLogo()
            Text(
                text = headerText,
                modifier = Modifier.padding(top = 20.dp),
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedTextField(
            value = uiStates.code,
            onValueChange = {
                viewModel.onEvent(CodeScreenUiActions.CodeChanged(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp),
            label = {
                Text(text = "Enter Code")
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Don't have code?",
                Modifier.clickable { navigateToSignInScreen() },
                color = Color(0XFF05A8B3),
                textDecoration = TextDecoration.Underline
            )
        }
        Button(
            onClick = {
                viewModel.onEvent(CodeScreenUiActions.Submit)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 10.dp, end = 10.dp)
        ) {
            Text("Submit")
        }
    }
}