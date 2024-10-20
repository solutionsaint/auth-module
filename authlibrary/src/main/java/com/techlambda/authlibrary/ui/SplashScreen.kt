package com.techlambda.authlibrary.ui

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.techlambda.authlibrary.ui.data.AuthPrefManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authPrefManager: AuthPrefManager,
    navigateToHomeScreen: () -> Unit,
    navHostController: NavHostController,
    projectId: String,
    appLogo: @Composable BoxScope.() -> Unit
) {
    val scale = remember {
        Animatable(0f)
    }
    val viewModel: SplashScreenViewModel = hiltViewModel()
    val uiEvents = viewModel.uiEvents.collectAsStateWithLifecycle(SplashScreenUiEvents.None).value


    LaunchedEffect(key1 = !authPrefManager.isLoggedIn()) {
        viewModel.onEvent(SplashScreenUiActions.SendProjectId(projectId = projectId))
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 2f, animationSpec = tween(durationMillis = 1000, easing = {
                OvershootInterpolator(4f).getInterpolation(it)
            })
        )
        if (authPrefManager.isLoggedIn()) {
            delay(2000)
            if (authPrefManager.getUserData()?.isAdmin == true) {
                navigateToHomeScreen()
            } else {
                navHostController.navigate(AppNavigation.CodeScreen)
            }
        }
    }
    when (uiEvents) {
        is SplashScreenUiEvents.OnError -> {
            navHostController.navigate(AppNavigation.SignInScreen)
        }

        is SplashScreenUiEvents.Success -> {
            Log.d("TAG", "SplashScreen: Call")
            navHostController.navigate(AppNavigation.SignInScreen)
        }

        else -> {}
    }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        appLogo()
    }
}