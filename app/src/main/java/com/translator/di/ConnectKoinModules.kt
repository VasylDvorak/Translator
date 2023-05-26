package com.translator.di


import androidx.room.Room
import com.example.favoritescreen.FavoriteInteractor
import com.example.favoritescreen.FavoriteViewModel
import com.example.model.data.DataModel
import com.example.model.datasource.RetrofitImplementation
import com.translator.di.koin_modules.ApiModule
import com.translator.di.koin_modules.AppModule
import com.translator.di.koin_modules.CiceroneModule
import com.translator.di.koin_modules.FavoriteFragmentModule
import com.translator.di.koin_modules.HistoryFragmentModule
import com.translator.di.koin_modules.MainFragmentModule
import com.translator.di.koin_modules.NAME_CICERONE_MODULE_CICERONE
import com.translator.datasource.RoomDataBaseImplementation
import com.example.room.HistoryFavoriteDataBase
import com.translator.view.main_fragment.MainInteractor
import com.translator.view.main_fragment.MainViewModel
import com.example.historyscreen.HistoryInteractor
import com.example.historyscreen.HistoryViewModel
import com.example.repository.Repository
import com.example.repository.RepositoryImplementation
import com.example.repository.RepositoryImplementationLocal
import com.example.repository.RepositoryLocal
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module


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
    }

    val historyScreen = module {
        single { HistoryFragmentModule().historyFragment() }
        factory { HistoryViewModel(interactor = get()) }
        factory { HistoryInteractor(get()) }
    }
    val favoriteScreen = module {
        single { FavoriteFragmentModule().favoriteFragment() }
        factory { FavoriteViewModel(interactor = get()) }
        factory { FavoriteInteractor(get()) }
    }

    val mainScreen = module {
        factory { MainViewModel(get()) }
        factory { MainInteractor(get(), get()) }
    }

    val apiModule = module {
        factory { ApiModule().getService() }

    }

    val appModule = module {
        single { AppModule().applicationContext(context = androidApplication()) }
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