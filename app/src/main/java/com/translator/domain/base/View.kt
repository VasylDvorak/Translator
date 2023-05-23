package com.translator.domain.base

import com.translator.model.data.AppState
import com.translator.model.data.DataModel

interface View {

    fun responseEmpty()

    fun responseHasData(dataModel: List<DataModel>)

    fun showViewLoading()

    fun appStateProgressNotEmpty(progress: Int)

    fun appStateProgressEmpty()

    fun showErrorScreen(error: String?)

    fun searchListener()

}
