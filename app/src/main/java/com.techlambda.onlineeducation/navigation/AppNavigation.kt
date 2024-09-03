package com.techlambda.onlineeducation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.techlambda.authlibrary.ui.code.CodeScreen
import com.techlambda.authlibrary.ui.code.CodeViewModel
import com.techlambda.authlibrary.ui.signUp.SignUpScreen
import com.techlambda.authlibrary.ui.signUp.SignUpViewModel
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpViewModel
import com.techlambda.authlibrary.ui.signUp.verifyOtp.VerifyOtpScreen
import com.techlambda.authlibrary.ui.signin.ResetPasswordScreen
import com.techlambda.authlibrary.ui.signin.SignInScreen
import com.techlambda.authlibrary.ui.signin.SignInViewModel
import com.techlambda.onlineeducation.R
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

        composable<AppNavigation.SignUpScreen> {
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                viewModel = signUpViewModel,
                onSignUpSuccess = { email ->
                    navHostController.navigate(AppNavigation.VerifyOtpScreen(email))
                },
                appLogo = {

                },
                onSignInClick = {
                    navHostController.navigate(AppNavigation.SignInScreen)
                }
            )
        }

        composable<AppNavigation.SignInScreen> {
            val signInViewModel: SignInViewModel = hiltViewModel()
            SignInScreen(
                viewModel = signInViewModel,
                onSignInSuccess = {
//                    navHostController.navigate(AppNavigation.Home)
                    navHostController.navigate(AppNavigation.CodeScreen)
                },
                onForgotPasswordClick = {
                    navHostController.navigate(AppNavigation.ForgotPasswordScreen)
                },
                appLogo = {

                },
                onSignUpClick = {
                    navHostController.navigate(AppNavigation.SignUpScreen)
                }
            )
        }

        composable<AppNavigation.VerifyOtpScreen> { navigationBackStackEntry ->
            val otpInViewModel: OtpViewModel = hiltViewModel()
            val argument = navigationBackStackEntry.toRoute<AppNavigation.VerifyOtpScreen>()
            VerifyOtpScreen(
                email = argument.emailId,
                viewModel = otpInViewModel,
                onOtpVerified = {
                    navHostController.navigate(AppNavigation.Home)
                })
        }

        composable<AppNavigation.ForgotPasswordScreen>{
            val signInViewModel: SignInViewModel = hiltViewModel()
            ResetPasswordScreen (
                viewModel = signInViewModel
            ){
                navHostController.navigate(AppNavigation.SignInScreen)
            }
        }

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

        composable<AppNavigation.CodeScreen> {
            val codeViewModel: CodeViewModel = hiltViewModel()
            CodeScreen(
                viewModel = codeViewModel,
                onCodeSuccess = {
                    navHostController.navigate(AppNavigation.Home)
                },
                navigateToSignInScreen = {
                    navHostController.navigate(AppNavigation.SignInScreen)
                },
                appLogo = {
                    Icon(
                        painter = painterResource( R.drawable.ic_flag ),
                        contentDescription = "Logo"
                    )
                },
                headerText = "Enter Code",
                userId = ""
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
    data object ForgotPasswordScreen

    @Serializable
    data object Home

    @Serializable
    data object CodeScreen
}