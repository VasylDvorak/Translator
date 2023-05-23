package com.translator.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.translator.view.main.MainFragment

class AndroidScreens : IScreens {
    override fun startMainFragment() = FragmentScreen { MainFragment.newInstance() }


}



