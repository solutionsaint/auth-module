package com.techlambda.authlibrary.ui.signUp

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.techlambda.common.ui.CommonButton
import kotlinx.coroutines.launch

@Composable
fun TermsAndCondition(isSignUp: Boolean) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
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
                    "",
                    Html.FROM_HTML_MODE_LEGACY
                )
            }
        })
    }
    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
        if (scrollState.value != scrollState.maxValue) {
            CommonButton (text = "Scroll To Bottom") {
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        } else {
            if (isSignUp) {
                CommonButton (text = "Accept & Continue") {
                    //TODO: Update Terms and Condition State
                }
            }else{
                CommonButton (text = "Scroll To Top") {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                }
            }
        }
    }
}