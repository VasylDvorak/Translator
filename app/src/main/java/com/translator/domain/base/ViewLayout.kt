package com.translator.domain.base

interface ViewLayout {

    fun responseEmpty() {}

    fun showViewLoading() {}

    fun showErrorScreen(error: String?) {}


}
