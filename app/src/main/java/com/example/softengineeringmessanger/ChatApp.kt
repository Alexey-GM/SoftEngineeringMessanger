package com.example.softengineeringmessanger

import android.app.Application
import com.example.softengineeringmessanger.di.AppComponent
import com.example.softengineeringmessanger.di.DaggerAppComponent
import io.realm.Realm
import io.realm.RealmConfiguration

class ChatApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initDagger()
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("chats.realm")
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}