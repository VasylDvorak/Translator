package com.translator.viewmodel

import com.translator.model.data.AppState
import kotlinx.coroutines.flow.Flow

interface Interactor<T : Any> {
    suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState>
}