package com.techlambda.onlineeducation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.ui.theme.blue


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentDestination?.route
    NavigationBar(containerColor = blue) {
        NavigationBarItem(icon = {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = LocalContext.current.getString(R.string.home)
            )
        },
            label = { Text(LocalContext.current.getString(R.string.home)) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            selected = currentRoute == AppNavigation.Home.toString(),
            onClick = { navController.navigate(AppNavigation.Home) })
        NavigationBarItem(icon = {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = LocalContext.current.getString(R.string.course)
            )
        },
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            label = { Text(LocalContext.current.getString(R.string.course)) },
            selected = currentRoute == AppNavigation.CourseSearch.toString(),
            onClick = { navController.navigate(AppNavigation.CourseSearch) })
        NavigationBarItem(icon = {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = LocalContext.current.getString(R.string.course)
            )
        },
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            label = { Text(LocalContext.current.getString(R.string.course)) },
            selected = currentRoute == AppNavigation.CourseSearch.toString(),
            onClick = { navController.navigate(AppNavigation.CourseSearch) })
        NavigationBarItem(icon = {
            Icon(
                imageVector = Icons.Filled.Build,
                contentDescription = LocalContext.current.getString(R.string.assignment)
            )
        },
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.tertiary),
            label = { Text(LocalContext.current.getString(R.string.assignment)) },
            selected = currentRoute == "other",
            onClick = { navController.navigate("other") })
    }

}


@Composable
fun MainScreen() {
    val navController = LocalNavigationProvider.current
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {}) { paddingValues ->
        AppNavHost(modifier = Modifier.padding(paddingValues))
    }
}
