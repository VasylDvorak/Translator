package com.translator.application

import android.app.Application
import com.translator.di.AppComponent


import com.translator.di.application_modules.AppModule
import com.translator.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    companion object {
        lateinit var instance: App
    }
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    lateinit var appComponent: AppComponent
        private set


    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .builderAppModule(AppModule(this))
            .build()
        appComponent.inject(this)

    }

}

