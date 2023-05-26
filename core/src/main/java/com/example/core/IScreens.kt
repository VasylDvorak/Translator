package com.example.core

import com.example.model.data.DataModel
import com.github.terrakok.cicerone.Screen


interface IScreens {

    fun startMainFragment(): Screen
    fun startDescriptionFragment(dataModel: DataModel): Screen
    fun startHistoryFragment(): Screen
    fun startFavoriteFragment(): Screen
}
