package com.lamarques.testecompose.core

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {
    @Provides
    @Singleton
    fun sharedPreferencesProvider(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE)
    }
}