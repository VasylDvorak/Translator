package com.translator.navigation

import android.os.Bundle
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.translator.model.data.DataModel
import com.translator.view.DescriptionFragment
import com.translator.view.favorite.FavoriteFragment
import com.translator.view.main_fragment.MainFragment
import com.translator.view.history.HistoryFragment

class AndroidScreens : IScreens {
    override fun startMainFragment() = FragmentScreen { MainFragment.newInstance() }

    override fun startDescriptionFragment(dataModel: DataModel): Screen = FragmentScreen {
        DescriptionFragment.newInstance(Bundle().apply {
            putParcelable(
                DescriptionFragment.CURRENT_DATA_MODEl,
                dataModel
            )
        })
    }

    override fun startHistoryFragment() = FragmentScreen { HistoryFragment.newInstance() }
    override fun startFavoriteFragment() = FragmentScreen { FavoriteFragment.newInstance() }


    fun getNull(argument: Int): Int? {
        if(argument>0){
            return 1
        }else{
            return null
        }
    }



}



