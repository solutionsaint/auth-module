package com.techlambda.authlibrary.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.techlambda.authlibrary.R
import com.techlambda.authlibrary.ui.code.CodeScreen
import com.techlambda.authlibrary.ui.code.CodeViewModel
import com.techlambda.authlibrary.ui.data.AuthPrefManager
import com.techlambda.authlibrary.ui.data.toUserData
import com.techlambda.authlibrary.ui.models.SignUpResponse
import com.techlambda.authlibrary.ui.signUp.SignUpScreen
import com.techlambda.authlibrary.ui.signUp.SignUpViewModel
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpViewModel
import com.techlambda.authlibrary.ui.signUp.verifyOtp.VerifyOtpScreen
import com.techlambda.authlibrary.ui.signin.ResetPasswordScreen
import com.techlambda.authlibrary.ui.signin.SignInScreen
import com.techlambda.authlibrary.ui.signin.SignInViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun AuthNavHost(
    modifier: Modifier,
    navHostController: NavHostController,
    onSignInSuccess: (SignUpResponse) -> Unit,
    appLogo: @Composable BoxScope.() -> Unit,
    onCodeSuccess: () -> Unit
) {
    val context = LocalContext.current
    val dataStore = AuthPrefManager(context)
    val coroutine = rememberCoroutineScope()
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = AppNavigation.SplashScreen,
    ) {

        composable<AppNavigation.SplashScreen> {
            SplashScreen(
                authPrefManager = dataStore,
                navHostController = navHostController,
                appLogo = appLogo,
                navigateToHomeScreen = onCodeSuccess
            )
        }

        composable<AppNavigation.SignUpScreen> {
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                viewModel = signUpViewModel,
                onSignUpSuccess = { email ->
                    navHostController.navigate(AppNavigation.VerifyOtpScreen(email))
                },
                appLogo = {
                    appLogo()
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
                onSignInSuccess = { signInResponse ->
                    coroutine.launch {
                        dataStore.saveUserData(signInResponse.toUserData())
                    }
                    if (signInResponse.userType.lowercase() == "admin"){
                        onSignInSuccess(signInResponse)
                    }else {
                        navHostController.navigate(AppNavigation.CodeScreen)
                    }
                },
                onForgotPasswordClick = {
                    navHostController.navigate(AppNavigation.ForgotPasswordScreen)
                },
                appLogo = {
                    appLogo()
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
                    navHostController.navigate(AppNavigation.SignInScreen)
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

        composable<AppNavigation.CodeScreen> {
            val codeViewModel: CodeViewModel = hiltViewModel()
            CodeScreen(
                viewModel = codeViewModel,
                onCodeSuccess = {
                    onCodeSuccess()
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
                headerText = "Enter Code"
            )
        }

    }

}

@Serializable
sealed class AppNavigation {

    @Serializable
    data object SplashScreen

    @Serializable
    data object SignInScreen

    @Serializable
    data object SignUpScreen

    @Serializable
    data class VerifyOtpScreen(val emailId: String)

    @Serializable
    data object ForgotPasswordScreen

    @Serializable
    data object CodeScreen
}