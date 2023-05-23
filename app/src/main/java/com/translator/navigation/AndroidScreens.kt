package com.translator.navigation

import android.os.Bundle
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.translator.model.data.DataModel
import com.translator.view.CURRENT_DATA_MODEl
import com.translator.view.DescriptionFragment
import com.translator.view.favorite.FavoriteFragment
import com.translator.view.main_fragment.MainFragment
import com.translator.view.history.HistoryFragment

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



