package com.translator.presenter

import com.github.terrakok.cicerone.Router
import com.translator.navigation.IScreens
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject


class MainPresenter {
    private val router: Router by inject(Router::class.java)
    fun mainFragmentStart() {
        val screen = getKoin().get<IScreens>()
        router.replaceScreen(screen.startMainFragment())
    }

    fun backClicked() { router.exit() }
}
