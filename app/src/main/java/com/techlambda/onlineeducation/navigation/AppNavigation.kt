package com.techlambda.onlineeducation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.techlambda.onlineeducation.ui.signin.SignInScreen
import com.techlambda.onlineeducation.ui.signUp.SignUpScreen
import com.techlambda.onlineeducation.customcanvas.BlueprintDrawer
import com.techlambda.onlineeducation.ui.adminModule.AddFloorSetupScreen
import com.techlambda.onlineeducation.ui.adminModule.AddInstituteScreen
import com.techlambda.onlineeducation.ui.adminModule.SetupCompleteScreen
import com.techlambda.onlineeducation.ui.adminModule.drawLayoutSetupScreen
import com.techlambda.onlineeducation.ui.signUp.verifyOtp.OtpScreen
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


        composable<AppNavigation.AddFloor> {
            AddFloorSetupScreen()
        }
        composable<AppNavigation.AddInstitute> {
            AddInstituteScreen()
        }
        composable<AppNavigation.AddLayout> {
            drawLayoutSetupScreen()
        }
        composable<AppNavigation.SetupComplete> {
            SetupCompleteScreen()
        }
        composable<AppNavigation.AddLayoutDraw> {
            BlueprintDrawer()
        }
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
    data object AddFloor

    @Serializable
    data object AddInstitute

    @Serializable
    data object AddLayout

    @Serializable
    data object AddLayoutDraw

    @Serializable
    data object SetupComplete

    @Serializable
    data object SignInScreen

    @Serializable
    data object SignUpScreen

    @Serializable
    data object SignupStudentScreen

    @Serializable
    data object SignUpInstructorScreen

    @Serializable
    data class VerifyOtpScreen(val emailId: String)

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
