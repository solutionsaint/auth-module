package com.techlambda.authlibrary.navigation

import androidx.navigation.NavHostController

interface NavCallback {
    fun navigateTo(navHostController: NavHostController, route: String)
}