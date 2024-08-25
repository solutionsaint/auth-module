// File: authlibrary/src/main/java/com/techlambda/authlibrary/di/OptionalInjectCheck.kt
package com.techlambda.authlibrary.di

import android.app.Activity
import androidx.fragment.app.Fragment
import dagger.hilt.android.migration.OptionalInject

object OptionalInjectCheck {
    fun wasInjectedByHilt(activity: Activity): Boolean {
        return activity::class.java.isAnnotationPresent(OptionalInject::class.java)
    }

    fun wasInjectedByHilt(fragment: Fragment): Boolean {
        return fragment::class.java.isAnnotationPresent(OptionalInject::class.java)
    }
}