package com.translator.di.application_modules

import com.translator.view.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment
}
