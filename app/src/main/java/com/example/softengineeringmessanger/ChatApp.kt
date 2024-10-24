package com.example.softengineeringmessanger

import android.app.Application
import com.example.softengineeringmessanger.di.AppComponent
import com.example.softengineeringmessanger.di.DaggerAppComponent

class ChatApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}