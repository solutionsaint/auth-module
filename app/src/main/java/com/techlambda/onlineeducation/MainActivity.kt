package com.techlambda.onlineeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.techlambda.onlineeducation.navigation.AppNavHost
import com.techlambda.onlineeducation.navigation.LocalNavigationProvider
import com.techlambda.onlineeducation.navigation.MainScreen
import com.techlambda.onlineeducation.ui.theme.VMTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VMTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val primaryColor = MaterialTheme.colorScheme.primary

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = primaryColor,
                        darkIcons = useDarkIcons
                    )
                }
                val navController = rememberNavController()
                CompositionLocalProvider(value = LocalNavigationProvider provides navController) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        MainScreen()
                    }
                }

            }
        }
    }
}