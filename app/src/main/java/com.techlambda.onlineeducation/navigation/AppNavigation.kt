package com.techlambda.onlineeducation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


val LocalNavigationProvider = staticCompositionLocalOf<NavHostController> {
    error("No NavHost")
}

@Composable
fun AppNavHost(modifier: Modifier) {
    val navHostController = LocalNavigationProvider.current
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = AppNavigation.Home,
    ) {

        composable<AppNavigation.Home> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Bharat",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }

}

@Serializable
sealed class AppNavigation {

    @Serializable
    data object Home
}