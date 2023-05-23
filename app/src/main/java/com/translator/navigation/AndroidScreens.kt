package com.translator.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.translator.view.MainFragment
import dagger.android.support.AndroidSupportInjection

class AndroidScreens : IScreens {
    override fun startMainFragment() = FragmentScreen { MainFragment.newInstance() }

}



