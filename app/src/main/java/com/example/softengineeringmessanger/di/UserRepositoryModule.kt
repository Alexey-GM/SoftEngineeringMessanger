package com.example.softengineeringmessanger.di

import com.example.softengineeringmessanger.data.UserRepositoryImpl
import com.example.softengineeringmessanger.data.nw.UserApiService
import com.example.softengineeringmessanger.data.nw.UserManager
import com.example.softengineeringmessanger.domain.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserRepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(apiService: UserApiService, userManager: UserManager): UserRepository {
        return UserRepositoryImpl(apiService, userManager)
    }
}
