package com.example.softengineeringmessanger.di

import com.example.softengineeringmessanger.data.nw.UserApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideUserApiService(): UserApiService {
        return UserApiService.createApiService()
    }
}