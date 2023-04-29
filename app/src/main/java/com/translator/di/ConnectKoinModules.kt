package com.translator.di


import com.translator.di.koin_modules.ApiModule
import com.translator.di.koin_modules.AppModule
import com.translator.di.koin_modules.MainFragmentModule
import com.translator.di.koin_modules.NAME_LOCAL
import com.translator.di.koin_modules.NAME_REMOTE
import com.translator.model.data.DataModel
import com.translator.model.datasource.RetrofitImplementation
import com.translator.model.datasource.RoomDataBaseImplementation
import com.translator.model.repository.Repository
import com.translator.model.repository.RepositoryImplementation
import com.translator.view.MainInteractor
import com.translator.view.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ConnectKoinModules {
    val application = module {
        single<Repository<List<DataModel>>>(named(NAME_REMOTE)) {
            RepositoryImplementation(
                RetrofitImplementation()
            )
        }
        single<Repository<List<DataModel>>>(named(NAME_LOCAL)) {
            RepositoryImplementation(
                RoomDataBaseImplementation()
            )
        }
    }

    val mainScreen = module {
        factory {
            MainInteractor(
                repositoryRemote = get(named(NAME_REMOTE)),
                repositoryLocal = get(named(NAME_LOCAL))
            )
        }
        factory { MainViewModel(interactor = get()) }
    }

    val apiModule = module {
        factory { ApiModule().getService() }

    }

    val appModule = module {
        single { AppModule().applicationContext(context = androidApplication()) }
    }


    val mainFragmentModule = module {
        single { MainFragmentModule().mainFragment() }

    }

}





