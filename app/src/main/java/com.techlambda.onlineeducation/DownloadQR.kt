package com.techlambda.onlineeducation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext


    @Composable
    fun DownloadQRCode(qrCodeBitmap: Bitmap) {
        val context = LocalContext.current
        var selectedFormat by remember { mutableStateOf("jpg") } // Default to JPG

        // Launcher for creating a file using Storage Access Framework
        val createFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    saveQRCodeToFile(context, qrCodeBitmap, uri, selectedFormat)
                }
            }
        }

        // Trigger the file creation intent
        Button(onClick = {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = if (selectedFormat == "jpg") "image/jpeg" else "image/png"
                putExtra(Intent.EXTRA_TITLE, "qrcode.${selectedFormat}")
            }
            createFileLauncher.launch(intent)
        }) {
            Text("Download QR Code")
        }

        // Option to choose format (JPG or PNG)
        Row {
            RadioButton(selected = selectedFormat == "jpg", onClick = { selectedFormat = "jpg" })
            Text("JPG")
            RadioButton(selected = selectedFormat == "png", onClick = { selectedFormat = "png" })
            Text("PNG")
        }
    }

    // Function to save the QR code to the selected file
    fun saveQRCodeToFile(context: Context, bitmap: Bitmap, uri: Uri, format: String) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                if (format == "jpg") {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                outputStream.flush()
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., file not found, permission issues)
        }
    }
