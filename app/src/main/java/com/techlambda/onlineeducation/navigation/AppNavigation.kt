package com.techlambda.onlineeducation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.techlambda.onlineeducation.ui.signUp.SignUpScreen
import com.techlambda.onlineeducation.ui.signUp.verifyOtp.OtpScreen
import com.techlambda.onlineeducation.ui.signin.SignInScreen
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
        startDestination = AppNavigation.SignInScreen,
    ) {
        composable<AppNavigation.SignInScreen> {
            SignInScreen()
        }
        composable<AppNavigation.SignUpScreen> {
            SignUpScreen()
        }
        composable<AppNavigation.VerifyOtpScreen> { navigationBackStackEntry ->
            val argument = navigationBackStackEntry.toRoute<AppNavigation.VerifyOtpScreen>()
            OtpScreen(email = argument.emailId)
        }


    }

}

@Serializable
sealed class AppNavigation {

    @Serializable
    data object SignInScreen

    @Serializable
    data object SignUpScreen

    @Serializable
    data class VerifyOtpScreen(val emailId: String)

    @Serializable
    data object Home

    @Serializable
    data object CourseSearch

}
