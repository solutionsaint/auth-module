package com.techlambda.authlibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.techlambda.authlibrary.ui.signUp.SignUpScreen
import com.techlambda.authlibrary.ui.signUp.verifyOtp.OtpScreen
import com.techlambda.authlibrary.ui.signin.ResetPasswordScreen
import com.techlambda.authlibrary.ui.signin.SignInScreen



@Composable
fun AppNavigation() {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = "SignInScreen",
    ) {
        composable("SignInScreen") {
            SignInScreen(navHostController = navHostController)
        }
        composable("SignUpScreen") {
            SignUpScreen(navHostController = navHostController)
        }
        composable("VerifyOtpScreen") { navigationBackStackEntry ->
            val emailId = navigationBackStackEntry.arguments?.getString("emailId") ?: ""
            OtpScreen(navHostController = navHostController, email = emailId)
        }
//        composable("QRCode") {
//            QRCodeScreen()
//        }
        composable("ResetPasswordScreen") {
            ResetPasswordScreen(
                navHostController = navHostController,
                onPasswordReset = {
                    navHostController.navigate("SignInScreen") // Navigate back to SignIn screen
                }
            )
        }
    }
}
