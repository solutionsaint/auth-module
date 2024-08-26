package com.techlambda.authlibrary.navigation

import androidx.navigation.NavHostController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface NavigationActions {
    fun navigateToHome(navController: NavHostController)
}

class DefaultNavigationActions : NavigationActions {
    override fun navigateToHome(navController: NavHostController) {
        navController.navigate(AppNavigation.QRCode)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationActions(): NavigationActions = DefaultNavigationActions()
}