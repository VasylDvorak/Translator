package com.translator.domain


import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.repository.Repository
import com.translator.viewmodel.Interactor
import io.reactivex.rxjava3.core.Observable

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource) {
            remoteRepository.getData(word).map { AppState.Success(it) }
        } else {
            localRepository.getData(word).map { AppState.Success(it) }
        }
    }
}
