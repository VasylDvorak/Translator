package com.translator.domain.base
interface View {

    fun responseEmpty()

    fun showViewLoading()

    fun appStateProgressNotEmpty(progress: Int)

    fun appStateProgressEmpty()

    fun showErrorScreen(error: String?)

    fun searchListener()

}
