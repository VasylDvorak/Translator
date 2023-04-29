package com.translator.application

import android.app.Application
import com.translator.di.ConnectKoinModules.apiModule
import com.translator.di.ConnectKoinModules.appModule
import com.translator.di.ConnectKoinModules.application
import com.translator.di.ConnectKoinModules.mainFragmentModule
import com.translator.di.ConnectKoinModules.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(application, mainScreen, apiModule, appModule,
           mainFragmentModule))
        }

    }

}

