package com.translator.view

import com.translator.di.application_modules.NAME_LOCAL
import com.translator.di.application_modules.NAME_REMOTE
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.repository.Repository
import com.translator.viewmodel.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Named

class MainInteractor @Inject constructor(
    @Named(NAME_REMOTE) val repositoryRemote: Repository<List<DataModel>>,
    @Named(NAME_LOCAL) val repositoryLocal: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource) {
            repositoryRemote
        } else {
            repositoryLocal
        }.getData(word).map { AppState.Success(it)  }
    }
}
