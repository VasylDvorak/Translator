package com.translator.domain.base
interface View {

    fun responseEmpty()

    fun showViewLoading()

    fun showErrorScreen(error: String?)


}
