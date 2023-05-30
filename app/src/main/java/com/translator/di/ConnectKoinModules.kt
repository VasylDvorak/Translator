package com.translator.di


import android.content.Context
import androidx.room.Room
import com.translator.di.koin_modules.ApiModule
import com.translator.di.koin_modules.AppModule
import com.translator.di.koin_modules.CiceroneModule
import com.translator.di.koin_modules.FavoriteFragmentModule
import com.translator.di.koin_modules.HistoryFragmentModule
import com.translator.di.koin_modules.MainFragmentModule
import com.translator.di.koin_modules.NAME_CICERONE_MODULE_CICERONE
import com.translator.model.data.DataModel
import com.translator.model.datasource.RetrofitImplementation
import com.translator.model.datasource.RoomDataBaseImplementation
import com.translator.model.repository.Repository
import com.translator.model.repository.RepositoryImplementation
import com.translator.model.repository.RepositoryImplementationLocal
import com.translator.model.repository.RepositoryLocal
import com.translator.room.HistoryFavoriteDataBase
import com.translator.utils.network.OnlineRepository
import com.translator.view.favorite.FavoriteFragment
import com.translator.view.favorite.FavoriteInteractor
import com.translator.view.favorite.FavoriteViewModel
import com.translator.view.history.HistoryFragment
import com.translator.view.history.HistoryInteractor
import com.translator.view.history.HistoryViewModel
import com.translator.view.main_fragment.MainInteractor
import com.translator.view.main_fragment.MainViewModel
import com.translator.view.main_fragment.MainFragment
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.mp.KoinPlatform.getKoin

object ConnectKoinModules {

    val application = module {
        single {
            Room.databaseBuilder(get(), HistoryFavoriteDataBase::class.java, "HistoryDB").build()
        }
        single { get<HistoryFavoriteDataBase>().historyDao() }
        single { get<HistoryFavoriteDataBase>().favoriteDao() }
        single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }
        single<RepositoryLocal<List<DataModel>>> {
            RepositoryImplementationLocal(RoomDataBaseImplementation(get(), get()))
        }
        single { OnlineRepository() }
    }

    val historyScreen = module {
        scope(named<HistoryFragment>()) {
            scoped { HistoryInteractor(get()) }
            viewModel { HistoryViewModel(get()) }
        }
    }

    val historyScreenScope by lazy {
        getKoin()
            .getOrCreateScope("historyScreenScope", named<HistoryFragment>())
    }


    val favoriteScreen = module {
        scope(named<FavoriteFragment>()) {
            scoped { FavoriteInteractor(get()) }
            viewModel { FavoriteViewModel(get()) }
        }
    }
    val favoriteScreenScope by lazy {
        getKoin()
            .getOrCreateScope("favoriteScreenScope", named<FavoriteFragment>())
    }


    val mainScreen = module {
        scope(named<MainFragment>()) {
            scoped { MainInteractor(get(), get()) }
            viewModel { MainViewModel(get()) }
        }
    }

    val mainScreenScope by lazy {
        getKoin()
            .getOrCreateScope("mainScreenScope", named<MainFragment>())
    }


    val apiModule = module {
        factory { ApiModule().getService() }

    }

    val appModule = module {
        scope(named<Context>()) {
            scoped { AppModule().applicationContext(context = androidApplication()) }
        }
    }

    val ciceroneModule = module {

        single(qualifier = named(NAME_CICERONE_MODULE_CICERONE)) { CiceroneModule().cicerone() }
        single {
            CiceroneModule().navigatorHolder(
                cicerone =
                get(named(NAME_CICERONE_MODULE_CICERONE))
            )
        }
        single { CiceroneModule().router(cicerone = get(named(NAME_CICERONE_MODULE_CICERONE))) }
        single { CiceroneModule().screens() }
    }


    val mainFragmentModule = module {
        single { MainFragmentModule().mainFragment() }

    }
    val historyFragmentModule = module {
        single { HistoryFragmentModule().historyFragment() }

    }

    val favoriteFragmentModule = module {
        single { FavoriteFragmentModule().favoriteFragment() }

    }
}