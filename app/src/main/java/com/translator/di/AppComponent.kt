package com.translator.di


import android.app.Application
import com.translator.application.App
import com.translator.di.application_modules.ApiModule
import com.translator.di.application_modules.AppModule
import com.translator.di.application_modules.CiceroneModule
import com.translator.di.application_modules.MainFragmentModule
import com.translator.di.application_modules.InteractorModule
import com.translator.di.application_modules.RepositoryModule
import com.translator.di.application_modules.ViewModelModule
import com.translator.model.datasource.RetrofitImplementation
import com.translator.view.main.MainActivity
import com.translator.presenter.MainPresenter
import com.translator.view.main.MainFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CiceroneModule::class,
        ApiModule::class,
        InteractorModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        MainFragmentModule::class,
        AndroidSupportInjectionModule::class
    ]
)


interface AppComponent {

    @Component.Builder
    interface Builder {

        fun builderAppModule(appModule: AppModule): Builder
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(englishVocabularyApp: App)
    fun inject(mainFragment: MainFragment)

    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)
    fun inject(retrofitImplementation: RetrofitImplementation)
}

