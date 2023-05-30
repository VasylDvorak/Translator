package com.translator.view.history

import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.repository.RepositoryLocal
import com.translator.viewmodel.Interactor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class HistoryInteractor(
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState> {
        return MutableStateFlow(
            AppState.Success(
                repositoryLocal.getData(word)
            )
        )
    }

    override suspend fun getWordFromDB(word: String): Flow<DataModel> {
        return MutableStateFlow(
            repositoryLocal.findWordInDB(word)
        )
    }

    override suspend fun putFavorite(favorite: DataModel) {
        repositoryLocal.putFavorite(favorite)
    }


}
