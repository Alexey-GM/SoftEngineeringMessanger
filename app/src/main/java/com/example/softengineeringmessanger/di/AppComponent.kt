package com.example.softengineeringmessanger.di

import android.app.Application
import com.example.softengineeringmessanger.ui.auth.LoginFragment
import com.example.softengineeringmessanger.ui.auth.LoginViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        UserRepositoryModule::class,
        SharedPreferencesModule::class,
        RealmModule::class,
        ChatRepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(loginViewModel: LoginViewModel)
    fun inject(loginFragment: LoginFragment)
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}