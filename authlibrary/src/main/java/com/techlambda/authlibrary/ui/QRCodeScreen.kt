package com.techlambda.authlibrary.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScreen() {
    val qrCodeBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val viewModel: OtpViewModel = hiltViewModel()
    val context = LocalContext.current
    val appName = context.packageName
    // Generate QR code
    println("appName: $appName")
    viewModel.generateQrCode(appName) { bitmap ->
        qrCodeBitmap.value = bitmap
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Home") })
        },
        bottomBar = {
            BottomAppBar {}
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    qrCodeBitmap.value?.let { bitmap ->
                        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "QR Code")
                        Spacer(modifier = Modifier.height(16.dp))
                        DownloadQRCode(qrCodeBitmap = bitmap)
                    }
                }
            }
        }
    }
}
@Composable
fun QrCodeScreen(viewModel: OtpViewModel = hiltViewModel()) {
    val qrCodeBitmap = remember { mutableStateOf<Bitmap?>(null) }

    viewModel.generateQrCode("https://play.google.com/store/apps/details?id=com.techlambda.onlineeducation") { bitmap ->
        qrCodeBitmap.value = bitmap
    }

    qrCodeBitmap.value?.let { bitmap ->
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "QR Code")
    }
}


@Preview
@Composable
fun PreviewQrCodeScreen() {
    QrCodeScreen()
}

@Preview
@Composable
fun PreviewHomeScreen() {
    QRCodeScreen()
}

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
        Row {
            Icon(Icons.Default.Download, contentDescription = "Download")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row{
            Text("Download")
        }
    }

    // Option to choose format (JPG or PNG)
    Row {
        Row {
            RadioButton(selected = selectedFormat == "jpg", onClick = { selectedFormat = "jpg" })
        }
        Row(modifier = Modifier.padding(0.dp,16.dp)){Text("JPG")}
        Row {
            RadioButton(selected = selectedFormat == "png", onClick = { selectedFormat = "png" })
        }
        Row(modifier = Modifier.padding(0.dp,16.dp)){Text("PNG")}
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
