package com.translator.view.favorite

import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.repository.RepositoryLocal
import com.translator.viewmodel.Interactor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FavoriteInteractor(
    val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState> {
        return MutableStateFlow(
            AppState.Success(
                repositoryLocal.getFavoriteList()
            )
        )
    }

    override suspend fun removeFavoriteItem(removeFavorite: DataModel) {
        repositoryLocal.removeFavoriteItem(removeFavorite)
    }

}
