package com.example.core.viewmodel

import com.example.model.data.AppState
import com.example.model.data.DataModel
import kotlinx.coroutines.flow.Flow

interface Interactor<T : Any> {
    suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState>
    suspend fun getWordFromDB(word: String): Flow<DataModel>
    suspend fun putFavorite(favorite: DataModel)
    suspend fun removeFavoriteItem(removeFavorite: DataModel)
}