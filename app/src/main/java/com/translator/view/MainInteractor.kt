package com.translator.view

import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.repository.Repository
import com.translator.viewmodel.Interactor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull

class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: Repository<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): StateFlow<AppState> {

        val listData = if (fromRemoteSource) {
            repositoryRemote
        } else {
            repositoryLocal
        }.getData(word)

        val appState = if (listData.isEmpty()) {
            AppState.Error(Throwable(""))
        } else {
            AppState.Success(listData)
        }

        return MutableStateFlow(appState)
    }
}

