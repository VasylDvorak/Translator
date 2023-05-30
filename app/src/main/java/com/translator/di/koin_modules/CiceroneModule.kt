package com.translator.di.koin_modules


import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.translator.navigation.AndroidScreens
import com.translator.navigation.IScreens


class CiceroneModule {

    fun cicerone(): Cicerone<Router> = Cicerone.create()
    fun navigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.getNavigatorHolder()
    fun router(cicerone: Cicerone<Router>): Router = cicerone.router
    fun screens(): IScreens = AndroidScreens()
}