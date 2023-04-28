package com.translator.viewmodel

import com.translator.model.data.AppState

interface Interactor<T : Any> {
   suspend fun getData(word: String, fromRemoteSource: Boolean): AppState
}