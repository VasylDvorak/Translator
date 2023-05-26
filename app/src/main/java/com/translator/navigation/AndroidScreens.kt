package com.translator.navigation

import android.os.Bundle
import com.example.core.IScreens
import com.example.descriptionscreen.CURRENT_DATA_MODEl
import com.example.descriptionscreen.DescriptionFragment
import com.example.historyscreen.HistoryFragment
import com.example.model.data.DataModel
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.example.favoritescreen.FavoriteFragment
import com.translator.view.main_fragment.MainFragment

class AndroidScreens : IScreens {
    override fun startMainFragment() = FragmentScreen { MainFragment.newInstance() }

    override fun startDescriptionFragment(dataModel: DataModel): Screen = FragmentScreen {
        DescriptionFragment.newInstance(Bundle().apply {
            putParcelable(
                CURRENT_DATA_MODEl,
                dataModel
            )
        })
    }

    override fun startHistoryFragment()= FragmentScreen { HistoryFragment.newInstance() }
    override fun startFavoriteFragment()=FragmentScreen { FavoriteFragment.newInstance() }
}



