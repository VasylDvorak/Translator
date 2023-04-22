package com.translator.view.main

import com.github.terrakok.cicerone.Router
import com.translator.navigation.IScreens

import javax.inject.Inject

class MainPresenter
   {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens
    fun mainFragmentStart () {

        router.replaceScreen(screens.startMainFragment())
    }

    fun backClicked () {
        router.exit()
    }
}
