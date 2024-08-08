package com.techlambda.onlineeducation.ui.adminModule

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.techlambda.onlineeducation.ui.common.ImageSelectionDialog
import com.techlambda.onlineeducation.ui.signin.SignInScreen

@Composable
fun addInstituteScreen() {
    var imageUri by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Upload Institute\n Images - Step 1",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(160.dp))

            Box(
                modifier = Modifier
                    .size(250.dp, 250.dp)
                    .padding(40.dp)
                    .border(1.dp, if (imageUri.isNotEmpty()) Color.Transparent else Color.Black)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable { showPopup = true },
                contentAlignment = Alignment.Center,
            ) {
                showImage(imageUri)
            }

        }

        Button(
            onClick = {
                if (imageUri.isEmpty()) {
                    Toast.makeText(context, "Upload Image", Toast.LENGTH_SHORT).show()
                } else {
                   // navigateToNextScreen()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF), contentColor = Color.White)
        ) {
            Text(text = "Submit")
        }
}
    if(showPopup) {
        ImageSelectionDialog(
            showDialog = showPopup,
            onDismiss = { showPopup = false },
            onImageSelected = { imageUri = it.toString() })
    }
}

@Composable
private fun showImage(imageUri: String){
    val uri = imageUri.toUri()
    if (imageUri.isNotEmpty()) {
        Image(
            painter = rememberAsyncImagePainter(model = uri), contentDescription = "Selected Uri", modifier = Modifier.fillMaxSize())
    }else{
        Row {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Upload"
            )
            Text(text = "Upload Institute Photo")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun addInstituePrev() {
    addInstituteScreen()
}