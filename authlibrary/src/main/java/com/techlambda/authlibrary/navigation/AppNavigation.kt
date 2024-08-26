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
import com.techlambda.authlibrary.ui.signin.NavigationCallback
import com.techlambda.authlibrary.ui.signin.ResetPasswordScreen
import com.techlambda.authlibrary.ui.signin.SignInScreen
import kotlinx.serialization.Serializable

val LocalNavigationProvider = staticCompositionLocalOf<NavHostController> {
    error("No NavHost")
}

@Composable
fun AppNavHost(modifier: Modifier,
               navigationActions: NavigationActions
) {
    val navHostController = LocalNavigationProvider.current
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = AppNavigation.SignUpScreen
    ) {
        composable<AppNavigation.SignInScreen> {
            SignInScreen(
                navigationCallback = object : NavigationCallback {
                    override fun navigateToHome() {
                        navigationActions.navigateToHome(navHostController)
                    }
                }
            )
        }
        composable<AppNavigation.SignUpScreen> {
            SignUpScreen()
        }
        composable<AppNavigation.VerifyOtpScreen> { navigationBackStackEntry ->
            val argument = navigationBackStackEntry.toRoute<AppNavigation.VerifyOtpScreen>()
            OtpScreen(email = argument.emailId)
        }
        composable<AppNavigation.QRCode> {
            QRCodeScreen()
        }
        composable<AppNavigation.ResetPasswordScreen> {
            ResetPasswordScreen(
                navHostController = navHostController,
                onPasswordReset = {
                    navHostController.navigate(AppNavigation.SignInScreen) // Navigate back to SignIn screen
                }
            )
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
    data object QRCode

    @Serializable
    data object ResetPasswordScreen

    @Serializable
    data object CourseSearch

}
