package com.translator.view.main_fragment

import com.example.core.viewmodel.Interactor
import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.repository.Repository
import com.example.repository.RepositoryLocal


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): StateFlow<AppState> {
        val appState: AppState

        if (fromRemoteSource) {
            appState = AppState.Success(repositoryRemote.getData(word))
            repositoryLocal.saveToDB(appState)
        } else {
            appState = AppState.Success(repositoryLocal.getData(word))
        }

        return MutableStateFlow(appState)
    }

    override suspend fun getWordFromDB(word: String): Flow<DataModel> {
        return MutableStateFlow(
            repositoryLocal.findWordInDB(word)
        )
    }

    override suspend fun putFavorite(favorite: DataModel) {
        repositoryLocal.putFavorite(favorite)
    }

    override suspend fun removeFavoriteItem(removeFavorite: DataModel) {}
}

