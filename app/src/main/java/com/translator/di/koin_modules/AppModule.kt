package com.translator.di.koin_modules


import android.content.Context
import com.translator.application.App
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler


class AppModule() {

    fun applicationContext(context: Context) = context

    fun mainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()

}