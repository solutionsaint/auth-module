package com.techlambda.onlineeducation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.ui.theme.orange
import com.techlambda.onlineeducation.utils.Constants.homeScreen
import com.techlambda.onlineeducation.utils.Constants.moreScreen
import com.techlambda.onlineeducation.utils.Constants.ourServicesScreen
import com.techlambda.onlineeducation.utils.Constants.productPortfolioScreen


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomAppBar(containerColor = orange) {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = LocalContext.current.getString(R.string.home)) },
            label = { Text(LocalContext.current.getString(R.string.home)) },
            selected = currentRoute == homeScreen,
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            onClick = {
                navController.navigate(homeScreen) {
                    popUpTo(homeScreen) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = LocalContext.current.getString(R.string.course)) },
            label = { Text(LocalContext.current.getString(R.string.course)) },
            selected = currentRoute == productPortfolioScreen,
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            onClick = {
                navController.navigate(productPortfolioScreen) {
                    popUpTo(homeScreen) {
                        saveState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Build, contentDescription = LocalContext.current.getString(R.string.assignment)) },
            label = { Text(LocalContext.current.getString(R.string.assignment)) },
            selected = currentRoute == ourServicesScreen,
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            onClick = {
                navController.navigate(ourServicesScreen) {
                    popUpTo(homeScreen) {
                        saveState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Menu, contentDescription = LocalContext.current.getString(R.string.more)) },
            label = { Text(LocalContext.current.getString(R.string.more)) },
            selected = currentRoute == moreScreen,
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            onClick = {
                navController.navigate(moreScreen) {
                    popUpTo(homeScreen) {
                        saveState = true
                    }
                }
            }
        )
    }
}
