package com.techlambda.onlineeducation.ui.adminModule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.navigation.AppNavigation
import com.techlambda.onlineeducation.navigation.LocalNavigationProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFloorSetupScreen() {
    val navController = LocalNavigationProvider.current
    var selectedFloor by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val floorOptions = listOf("1", "2", "3", "4", "5")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Add Floor\n  Setup 2",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3F51B5),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.floor_plan), // Replace with your image resource
            contentDescription = "Floor Plan",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Select Floors",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedFloor,
                onValueChange = { selectedFloor = it },
                readOnly = true,
                label = { Text("Select Floors") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                floorOptions.forEach { floor ->
                    DropdownMenuItem(
                        onClick = {
                            selectedFloor = floor
                            expanded = false
                        },
                        text = { Text(text = floor) })
                }
            }
        }
    }
}
        Button(
            onClick = {
                navController.navigate(AppNavigation.AddLayout)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF), contentColor = Color.White),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(text = "Add Floor", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddFloorSetupScreenPreview() {
    AddFloorSetupScreen()
}