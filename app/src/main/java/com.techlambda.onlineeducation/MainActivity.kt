package com.techlambda.onlineeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.techlambda.authlibrary.ui.AuthNavHost
import com.techlambda.onlineeducation.navigation.AppNavHost
import com.techlambda.onlineeducation.navigation.LocalNavigationProvider
import com.techlambda.onlineeducation.ui.theme.VMTheme
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
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
                var showDashboard by remember { mutableStateOf(false) }
                CompositionLocalProvider(value = LocalNavigationProvider provides navController) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        if(showDashboard) {
                            AppNavHost(modifier = Modifier)
                        }else {
                            AuthNavHost(
                                modifier = Modifier,
                                appLogo = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_flag),
                                        contentDescription = "Logo"
                                    )
                                },
                                onCodeSuccess = {
                                    showDashboard = true
                                },
                                navHostController = navController,
                                onSignInSuccess = {
                                    showDashboard = true
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}