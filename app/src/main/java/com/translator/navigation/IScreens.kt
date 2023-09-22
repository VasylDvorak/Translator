package com.translator.navigation

import com.github.terrakok.cicerone.Screen
import com.translator.model.data.DataModel


interface IScreens {

    fun startMainFragment(): Screen
    fun startDescriptionFragment(dataModel: DataModel): Screen
    fun startHistoryFragment(): Screen
    fun startFavoriteFragment(): Screen
}
