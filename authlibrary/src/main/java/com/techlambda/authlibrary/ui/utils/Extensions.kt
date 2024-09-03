package com.techlambda.authlibrary.ui.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}



object LoaderManager {
    var isLoading = mutableStateOf(false)

    fun setLoadingState(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }
}

// Extension function to change the loading state
fun setLoading(isLoading: Boolean) {
    LoaderManager.setLoadingState(isLoading)
}

