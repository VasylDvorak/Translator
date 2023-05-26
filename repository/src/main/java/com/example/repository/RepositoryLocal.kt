package com.example.repository

import com.example.model.data.AppState
import com.example.model.data.DataModel



interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
    suspend fun findWordInDB(word: String): DataModel
    suspend fun putFavorite(favorite: DataModel)
    suspend fun removeFavoriteItem(favorite: DataModel)

}
