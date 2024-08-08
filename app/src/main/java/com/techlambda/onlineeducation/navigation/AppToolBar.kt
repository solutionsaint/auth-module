package com.techlambda.onlineeducation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.techlambda.onlineeducation.ui.theme.blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar(title:String) {
    TopAppBar(
        title = { Text(text = title) } ,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = blue,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}