package com.techlambda.onlineeducation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.techlambda.onlineeducation.utils.Constants.splashScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = splashScreen) {
        composable(splashScreen) {
          Box(){

          }
        }
    }
}
