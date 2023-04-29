package com.translator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.translator.model.data.AppState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel<T : AppState>(
    protected val liveDataForViewToObserve: MutableLiveData<T> = MutableLiveData(),
    var savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel(){

    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })




    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }
    protected fun cancelJob () {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }
    open fun getData(word: String, isOnline: Boolean): LiveData<T> = liveDataForViewToObserve

    abstract fun handleError (error: Throwable )


}
