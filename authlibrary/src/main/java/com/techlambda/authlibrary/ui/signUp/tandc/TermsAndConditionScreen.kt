package com.techlambda.authlibrary.ui.signUp.tandc

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.techlambda.common.ui.CommonButton
import kotlinx.coroutines.launch

@Composable
fun TermsAndCondition(isSignUp: Boolean, navController: NavController) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val viewModel: TermsAndConditionViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsStateWithLifecycle().value
    val uiEvents = viewModel.uiEvents.collectAsStateWithLifecycle(TermsAndConditionUiEvents.None).value
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = uiEvents) {
        when (uiEvents) {
            TermsAndConditionUiEvents.None -> {

            }

            is TermsAndConditionUiEvents.OnError -> {
                errorMessage = uiEvents.message
                showErrorDialog = true
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Terms and Condition Error") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(onClick = {
                    showErrorDialog = false
                    navController.popBackStack()
                }) {
                    Text("Try Again")
                }
            }
        )
    }
    if (!uiState.termsAndCondition.isNullOrBlank()) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(
                    bottom = 35.dp,
                    top = 10.dp,
                    start = 10.dp,
                    end = 10.dp
                )
        ) {
            AndroidView(factory = { context ->
                TextView(context).apply {
                    text = Html.fromHtml(
                        uiState.termsAndCondition,
                        Html.FROM_HTML_MODE_LEGACY
                    )
                }
            })
        }
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
            if (scrollState.value != scrollState.maxValue) {
                CommonButton(text = "Scroll To Bottom", modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            } else {
                if (isSignUp) {
                    CommonButton(text = "Accept & Continue", modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                        navController.previousBackStackEntry?.savedStateHandle?.set("isTermsAccepted", true)
                        navController.popBackStack()
                    }
                } else {
                    CommonButton(text = "Scroll To Top",  modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                }
            }
        }
    } else {
        LaunchedEffect(Unit) {
            viewModel.getTermsAndCondition()
        }
    }
}