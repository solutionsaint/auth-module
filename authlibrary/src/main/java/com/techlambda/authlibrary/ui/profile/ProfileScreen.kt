package com.techlambda.authlibrary.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlambda.common.ui.CommonButton
import com.techlambda.common.ui.InputField

@Composable
fun ProfileScreen(onUpdateSuccess: () -> Unit) {
    var isEditMode by remember { mutableStateOf(false) }
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsStateWithLifecycle().value
    val uiEvents = viewModel.uiEvents.collectAsStateWithLifecycle(ProfileUiEvents.None).value
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = uiEvents) {
        when (uiEvents) {
            ProfileUiEvents.None -> {

            }

            is ProfileUiEvents.OnError -> {
                errorMessage = uiEvents.message
                showErrorDialog = true
            }

            is ProfileUiEvents.UpdateSuccess -> {
                onUpdateSuccess()
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Update Error") },
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

    Column {
        Text(
            text = "Profile",
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 20.dp)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputField(label = "Name", value = uiState.name) {
                        viewModel.onEvent(ProfileUiActions.NameChanged(it))
                        isEditMode = true
                    }
                    InputField(label = "Email", value = uiState.email) {
                        viewModel.onEvent(ProfileUiActions.EmailChanged(it))
                        isEditMode = true
                    }
                    InputField(label = "Phone Number", value = uiState.number) {
                        viewModel.onEvent(ProfileUiActions.NumberChanged(it))
                        isEditMode = true
                    }
                    if (isEditMode) {
                        CommonButton(text = "Update Profile") {
                            viewModel.onEvent(ProfileUiActions.Update)
                        }
                    }
                }
            }
        }
    }
}