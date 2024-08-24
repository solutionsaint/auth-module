package com.techlambda.authlibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.techlambda.authlibrary.ui.QRCodeScreen
import com.techlambda.authlibrary.ui.signUp.SignUpScreen
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpScreen
import com.techlambda.authlibrary.ui.signin.ResetPasswordScreen
import com.techlambda.authlibrary.ui.signin.SignInScreen
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
        startDestination = AppNavigation.SignUpScreen::class.toString()
    ) {
        composable(AppNavigation.SignInScreen::class.toString()) {
            SignInScreen()
        }
        composable(AppNavigation.SignUpScreen::class.toString()) {
            SignUpScreen()
        }
        composable(AppNavigation.VerifyOtpScreen::class.toString()) { navigationBackStackEntry ->
            val emailId = navigationBackStackEntry.arguments?.getString("emailId") ?: ""
            OtpScreen(email = emailId)
        }
        composable(AppNavigation.QRCode::class.toString()) {
            QRCodeScreen()
        }
        composable(AppNavigation.ResetPasswordScreen::class.toString()) {
            ResetPasswordScreen(
                navHostController = navHostController,
                onPasswordReset = {
                    navHostController.navigate(AppNavigation.SignInScreen::class.toString()) // Navigate back to SignIn screen
                }
            )
        }
    }
}

sealed class AppNavigation {

    data object SignInScreen

    data object SignUpScreen

    data class VerifyOtpScreen(val emailId: String)

    data object Home

    data object QRCode

    data object ResetPasswordScreen


    data object CourseSearch

}
