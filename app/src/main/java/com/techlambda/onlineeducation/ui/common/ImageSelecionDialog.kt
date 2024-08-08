package com.techlambda.onlineeducation.ui.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun ImageSelectionDialog(
    showDialog: Boolean,
    requireDocumentUpload: Boolean = false,
    onDismiss: () -> Unit,
    onImageSelected: (Uri?) -> Unit
) {
    if (showDialog) {
        // Temporary variable to store the URI for captured image
        var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

        val context = LocalContext.current

        // Launcher for taking a picture
        val takePictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                onImageSelected(cameraImageUri)
            } else {
                onImageSelected(null)
            }
            onDismiss()
        }

        // Launcher for selecting an image from the gallery
        val selectFromGalleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            onImageSelected(uri)
            onDismiss()
        }

        // Launcher for selecting document
        val selectDocumentLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            onImageSelected(uri)
            onDismiss()
        }

        // Handle capturing an image
        val onCaptureImageClick = {
            val imageFile =
                File(context.externalCacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
            cameraImageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                imageFile
            )
            takePictureLauncher.launch(cameraImageUri!!)
        }

        // Handle selecting an image from the gallery
        val onSelectFromGalleryClick = {
            selectFromGalleryLauncher.launch("image/*")
        }

        // Handle selecting a document
        val onUploadDocumentClick = {
            selectDocumentLauncher.launch(arrayOf("application/pdf"))
        }
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = containerColor
                    ),
                color = containerColor
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .width(250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = "Choose an option"
                    )
                    Button(onClick = { onCaptureImageClick() },
                        modifier = Modifier.width(240.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(0XFF05A8B3)
                        ), shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Text(text = "Take Photo")
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { onSelectFromGalleryClick() },
                        modifier = Modifier.width(240.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(0XFF05A8B3)
                        ), shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Text(text = "Select from Gallery")
                    }
                    if (requireDocumentUpload) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick ={ onUploadDocumentClick()},
                            modifier = Modifier.width(240.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color(0XFF05A8B3)
                        ),shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                              Text(text = "Upload Document")
                        }
                    }
                }
            }
        }
    }
}