package com.translator.model.repository

import com.translator.model.data.AppState
import com.translator.model.data.DataModel


interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
    suspend fun findWordInDB(word: String): DataModel
    suspend fun putFavorite(favorite: DataModel)
    suspend fun removeFavoriteItem(favorite: DataModel)

}
