// File: authlibrary/src/main/java/com/techlambda/authlibrary/ui/signUp/SignUpActivity.kt
package com.techlambda.authlibrary.ui.signUp

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.techlambda.authlibrary.di.OptionalInjectCheck
import com.techlambda.authlibrary.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.migration.OptionalInject
import javax.inject.Inject

@OptionalInject
@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: HiltViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavigation()
        }

        if (OptionalInjectCheck.wasInjectedByHilt(this)) {
            Log.d("SignUpActivity", "Hilt injected the dependencies")
        } else {
            Log.e("SignUpActivity", "Hilt did not inject the dependencies")
        // Handle the case where Hilt did not inject the dependencies
        }
    }
}