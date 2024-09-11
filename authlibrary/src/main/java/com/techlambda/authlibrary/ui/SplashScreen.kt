package com.techlambda.authlibrary.ui

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.techlambda.authlibrary.ui.data.AuthPrefManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authPrefManager: AuthPrefManager,
    navigateToHomeScreen: () -> Unit,
    navHostController: NavHostController,
    appLogo: @Composable BoxScope.() -> Unit
) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 2f, animationSpec = tween(durationMillis = 1000, easing = {
                OvershootInterpolator(4f).getInterpolation(it)
            })
        )
        delay(2000L)
        if (authPrefManager.isLoggedIn()) {
            if (authPrefManager.getUserData()?.isAdmin == true) {
                navigateToHomeScreen()
            } else {
                navHostController.navigate(AppNavigation.CodeScreen)
            }
        } else {
            navHostController.navigate(AppNavigation.SignInScreen)
        }
    }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        appLogo()
    }
}