package com.translator.di


import com.translator.di.application_modules.ApiModule
import com.translator.di.application_modules.AppModule
import com.translator.di.application_modules.CiceroneModule
import com.translator.model.datasource.RetrofitImplementation
import com.translator.view.main.MainActivity
import com.translator.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CiceroneModule::class,
        ApiModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)
    fun inject(retrofitImplementation: RetrofitImplementation)

}