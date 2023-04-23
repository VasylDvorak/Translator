package com.translator.presenter
import com.translator.model.data.AppState
import com.translator.domain.base.View
import io.reactivex.rxjava3.observers.DisposableObserver

interface Presenter<T : AppState, V : View> {

    fun attachView(view: V)
    fun detachView(view: V)
    fun getData(word: String, isOnline: Boolean)

}
