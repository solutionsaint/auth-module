package com.techlambda.onlineeducation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.techlambda.onlineeducation.ui.signin.SignInScreen
import com.techlambda.onlineeducation.ui.signin.SignUpScreen
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
        composable<AppNavigation.SignupStudentScreen> {
            SignUpScreen()
        }
        composable<AppNavigation.SignUpInstructorScreen> {}
        composable<AppNavigation.VerifyOtpScreen> {

        }
        composable<AppNavigation.MyCourse> {}
        composable<AppNavigation.CourseDetailsScreen> {}
        composable<AppNavigation.CourseCreateScreen> {}
        composable<AppNavigation.CourseCreateScreen> {}


    }

}

@Serializable
sealed class AppNavigation {
    @Serializable
    data object Splash

    @Serializable
    data object SignInScreen

    @Serializable
    data object SignUpScreen

    @Serializable
    data object SignupStudentScreen

    @Serializable
    data object SignUpInstructorScreen

    @Serializable
    data object VerifyOtpScreen

    @Serializable
    data object Home

    @Serializable
    data object CourseSearch

    @Serializable
    data object MyCourse

    @Serializable
    data object MyAccount

    @Serializable
    data object CourseDetailsScreen

    @Serializable
    data object CourseCreateScreen

}
