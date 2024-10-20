package com.techlambda.onlineeducation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDiModule {

    @Singleton
    @Provides
    fun appContext(@ApplicationContext context: Context): Context {
        return context
    }
}
