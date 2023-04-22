package com.translator.application

import android.app.Application
import com.translator.di.AppComponent
import com.translator.di.DaggerAppComponent
import com.translator.di.application_modules.AppModule

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent
        private set


    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}
