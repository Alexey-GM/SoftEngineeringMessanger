package com.example.softengineeringmessanger.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.softengineeringmessanger.data.nw.UserManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserManager(sharedPreferences: SharedPreferences): UserManager {
        return UserManager(sharedPreferences)
    }
}