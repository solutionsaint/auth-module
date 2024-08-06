package com.techlambda.onlineeducation.ui.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.navigation.BottomNavigationBar
import com.techlambda.onlineeducation.navigation.ToolBar
import com.techlambda.onlineeducation.utils.Constants

@Composable
fun MoreScreen(navController: NavHostController) {
    val context = LocalContext.current

    val moreItemsData = listOf(
        MoreItemData(
            text = context.getString(R.string.about_us),
            icon = Icons.Default.Person
        ),
        MoreItemData(
            text = context.getString(R.string.exams),
            icon = ImageVector.vectorResource(id = R.drawable.ic_contibutors)
        )
    )
    Scaffold(
        topBar = { ToolBar(title = LocalContext.current.getString(R.string.more)) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), // Added padding to leave space at the bottom
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(moreItemsData.size) { option ->
                MoreItem(
                    text = moreItemsData[option].text,
                    icon = moreItemsData[option].icon
                ) {
                    when (it) {
                        context.getString(R.string.about_us) -> {
                            navController.navigate(Constants.aboutUs)
                        }

                        context.getString(R.string.exams) -> {
                            navController.navigate(Constants.contributorScreen)
                        }
                    }
                }
            }
        }
    }
}

data class MoreItemData(val text: String, val icon: ImageVector)


@Composable
fun MoreItem(text: String, icon: ImageVector, onClick: (text: String) -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .clickable { onClick(text) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Canvas(modifier = Modifier.fillMaxWidth()) {
            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(0f, size.height / 4),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height / 4),
                strokeWidth = 5f
            )
        }
    }
}

