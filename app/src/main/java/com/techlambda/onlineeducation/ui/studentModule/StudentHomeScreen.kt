package com.techlambda.onlineeducation.ui.studentModule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.ui.adminModule.AddFloorSetupScreen

@Composable
fun StudentHomeScreen(studentName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome\n $studentName",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap to view Institute",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        // Placeholder for the image of the school or building

        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.school_building),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun StudentHomeScreenPrev() {
    StudentHomeScreen(studentName = "{Student Name}")
}
