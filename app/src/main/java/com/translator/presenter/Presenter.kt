package com.translator.presenter
import com.translator.model.data.AppState
import com.translator.domain.base.View

interface Presenter<T : AppState, V : View> {

    fun attachView(view: V)
    fun detachView(view: V)
    fun getData(word: String, isOnline: Boolean)
}
