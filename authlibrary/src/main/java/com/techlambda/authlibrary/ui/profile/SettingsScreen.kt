package com.techlambda.authlibrary.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlambda.authlibrary.ui.data.AuthPrefManager
import com.techlambda.authlibrary.ui.data.TokenManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navigateToProfileScreen: () -> Unit,
    navigateToChangePasswordScreen : () -> Unit,
    navigateToTermsAndCondition: () -> Unit,
    navigateToWelcomeScreen : () -> Unit
) {
    val context = LocalContext.current
    val authPref = AuthPrefManager(context)
    val tokenManager = TokenManager(context)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 16.dp), // Added padding to leave space at the bottom
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        items(settingsOptions.size) { option ->
            SettingsOption(
                text = settingsOptions[option].text,
                icon = settingsOptions[option].icon
            ) {
                when (it) {
                    "Profile" -> {
                        navigateToProfileScreen()
                    }

                    "Change Password" -> {
                        navigateToChangePasswordScreen()
                    }

                    "Terms and Conditions" -> {
                        navigateToTermsAndCondition()
                    }

                    "Logout" -> {
                        GlobalScope.launch {
                            authPref.clearUserData()
                            tokenManager.clearTokens()
                        }
                        navigateToWelcomeScreen()
                    }
                }
            }
        }
    }
}

data class SettingsOptionData(val text: String, val icon: ImageVector)

val settingsOptions = listOf(
    SettingsOptionData(
        text = "Profile",
        icon = Icons.Default.Person
    ),
    /*SettingsOptionData(
        text = "Change Password",
        icon = Icons.Default.Lock
    ), */
    SettingsOptionData(
        text = "Terms and Conditions",
        icon = Icons.Default.Info
    ),
    SettingsOptionData(
        text = "Logout",
        icon = Icons.AutoMirrored.Default.ExitToApp
    )
)

@Composable
fun SettingsOption(text: String, icon: ImageVector, onClick: (text: String) -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .background(Color.White)
                .clickable { onClick(text) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Canvas(modifier = Modifier.fillMaxWidth()) {
            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(0f, size.height / 2),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2),
                strokeWidth = 5f
            )
        }
    }
}

