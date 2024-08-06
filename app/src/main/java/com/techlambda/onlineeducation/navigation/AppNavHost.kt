package com.techlambda.onlineeducation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.techlambda.onlineeducation.ui.screen.AboutUsScreen
import com.techlambda.onlineeducation.ui.screen.ExamsScreen
import com.techlambda.onlineeducation.ui.screen.HomeScreen
import com.techlambda.onlineeducation.ui.screen.MoreScreen
import com.techlambda.onlineeducation.ui.screen.CoursesScreen
import com.techlambda.onlineeducation.ui.screen.AssignmentsScreen
import com.techlambda.onlineeducation.ui.screen.SplashScreen
import com.techlambda.onlineeducation.utils.Constants.aboutUs
import com.techlambda.onlineeducation.utils.Constants.contributorScreen
import com.techlambda.onlineeducation.utils.Constants.homeScreen
import com.techlambda.onlineeducation.utils.Constants.moreScreen
import com.techlambda.onlineeducation.utils.Constants.ourServicesScreen
import com.techlambda.onlineeducation.utils.Constants.productPortfolioScreen
import com.techlambda.onlineeducation.utils.Constants.splashScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = splashScreen) {
        composable(splashScreen) {
            SplashScreen(navController)
        }

        composable(homeScreen) {
            HomeScreen(navController)
        }

        composable(aboutUs) {
            AboutUsScreen(navController)
        }

        composable(ourServicesScreen) {
            AssignmentsScreen(navController)
        }

        composable(productPortfolioScreen) {
            CoursesScreen(navController)
        }

        composable(contributorScreen) {
            ExamsScreen(navController)
        }

        composable(moreScreen) {
            MoreScreen(navController)
        }
    }
}
