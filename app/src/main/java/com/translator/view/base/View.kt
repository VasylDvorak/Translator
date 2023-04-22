package com.translator.view.base

import com.translator.model.data.AppState


interface View {

    fun renderData(appState: AppState)

}
