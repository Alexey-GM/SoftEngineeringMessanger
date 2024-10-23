package com.example.softengineeringmessanger.di

import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class RealmModule {
    @Provides
    @Singleton
    fun provideRealmInstance(): Realm {
        return Realm.getDefaultInstance()
    }
}