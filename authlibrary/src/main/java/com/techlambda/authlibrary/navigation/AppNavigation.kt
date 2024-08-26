package com.techlambda.authlibrary.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.techlambda.authlibrary.ui.HomeScreen
import com.techlambda.authlibrary.ui.QRCodeScreen
import com.techlambda.authlibrary.ui.signUp.SignUpScreen
import com.techlambda.authlibrary.ui.signUp.SignUpViewModel
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpScreen
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpViewModel
import com.techlambda.authlibrary.ui.signin.ResetPasswordScreen
import com.techlambda.authlibrary.ui.signin.SignInScreen
import com.techlambda.authlibrary.ui.signin.SignInViewModel
import kotlinx.serialization.Serializable


@Composable
fun AppNavHost(
    navController: NavHostController, startLocation: String, drawerState: DrawerState
) {
    NavHost(navController = navController, startDestination = startLocation) {
        composable(AppNavigation.SignInScreen.route) {
            val viewModel: SignInViewModel = hiltViewModel()
            SignInScreen(
                navController = navController,
                viewModel = viewModel,
                onSignInSuccess = {
                    navController.navigate(AppNavigation.Home.route) {
                        popUpTo(AppNavigation.SignInScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppNavigation.SignUpScreen.route) {
            val viewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                navController = navController,
                viewModel = viewModel,
                onSignUpSuccess = {
                    navController.navigate(AppNavigation.Home.route) {
                        popUpTo(AppNavigation.SignUpScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppNavigation.VerifyOtpScreen.route) {
            val viewModel: OtpViewModel = hiltViewModel()
            OtpScreen(
                navController = navController,
                email = "user@example.com", // Replace with actual email parameter
                viewModel = viewModel,
                onOtpVerified = {
                    navController.navigate(AppNavigation.Home.route) {
                        popUpTo(AppNavigation.VerifyOtpScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppNavigation.ResetPasswordScreen.route) {
            val viewModel: SignInViewModel = hiltViewModel()
            ResetPasswordScreen(
                navController = navController,
                viewModel = viewModel,
                onPasswordReset = {
                    navController.navigate(AppNavigation.SignInScreen.route) {
                        popUpTo(AppNavigation.ResetPasswordScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppNavigation.Home.route) {
            HomeScreen()
        }

        composable(AppNavigation.QRCode.route) {
            QRCodeScreen()
        }
    }
}


@Serializable
sealed class AppNavigation {

    @Serializable
    data object SignInScreen {
        const val route = "sign_in"
    }

    @Serializable
    data object SignUpScreen {
        const val route = "sign_up"
    }

    @Serializable
    data object VerifyOtpScreen {
        const val route = "verify_otp"
    }

    @Serializable
    data object ResetPasswordScreen {
        const val route = "reset_password"
    }

    @Serializable
    data object Home {
        const val route = "home"
    }

    @Serializable
    data object QRCode {
        const val route = "qr_code"
    }

    @Serializable
    data object CourseSearch {
        const val route = "course_search"
    }


}