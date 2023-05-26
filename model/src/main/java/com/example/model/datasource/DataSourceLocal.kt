package com.example.model.datasource

import com.example.model.data.AppState
import com.example.model.data.DataModel



interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)
    suspend fun findWordFromDB(word: String): DataModel
    suspend fun putFavorite(favorite: DataModel)
    suspend fun getFavoriteList(): List<DataModel>
    suspend fun removeFavoriteItem(favorite: DataModel)
}
