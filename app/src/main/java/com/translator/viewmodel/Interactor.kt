package com.translator.viewmodel

import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Interactor<T : Any> {
    suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState>
    suspend fun getWordFromDB(word: String): Flow<DataModel> = flow {}
    suspend fun putFavorite(favorite: DataModel) {}
    suspend fun removeFavoriteItem(removeFavorite: DataModel) {}
}