package com.translator.utils

import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.data.Meanings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


suspend fun parseSearchResults(state: Flow<AppState>): AppState {
    val newSearchResults = ArrayList<DataModel>()
    var appState = state.first()

    when (appState) {
        is AppState.Success -> {
            var searchResults = appState.data!!

            if (!searchResults.isNullOrEmpty()) {
                for (searchResult in searchResults) {
                    parseResult(searchResult, newSearchResults)
                }
            }
            appState = AppState.Success(newSearchResults)
        }

        else -> {}
    }
    return appState
}

private fun parseResult(dataModel: DataModel, newDataModels: ArrayList<DataModel>) {
    if (!dataModel.text.isNullOrBlank() && !dataModel.meanings.isNullOrEmpty()) {
        val newMeanings = arrayListOf<Meanings>()
        for (meaning in dataModel.meanings) {
            if (meaning.translation != null && !meaning.translation.translation.isNullOrBlank()) {
                newMeanings.add(
                    Meanings(
                        meaning.translation,
                        meaning.imageUrl, meaning.transcription, meaning.soundUrl
                    )
                )
            }
        }


        if (newMeanings.isNotEmpty()) {
            newDataModels.add(DataModel(dataModel.text, newMeanings))
        }
    }
}


