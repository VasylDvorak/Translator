package com.example.favoritescreen


import com.example.core.viewmodel.Interactor
import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.repository.RepositoryLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow


class FavoriteInteractor(
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState> {
        return MutableStateFlow(
            AppState.Success(
                repositoryLocal.getFavoriteList()
            )
        )
    }

    override suspend fun getWordFromDB(word: String): Flow<DataModel> = flow { }

    override suspend fun putFavorite(favorite: DataModel) {}
    override suspend fun removeFavoriteItem(removeFavorite: DataModel) {
        repositoryLocal.removeFavoriteItem(removeFavorite)
    }

}
