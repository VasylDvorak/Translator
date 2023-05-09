package com.translator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.translator.model.data.AppState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<T : AppState>(
    protected val liveDataForViewToObserve: MutableLiveData<T> = MutableLiveData(),
    var savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    protected var queryStateFlow = MutableStateFlow("")
    protected val job: Job = Job()


    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    protected fun cancelJob() {
        queryStateFlow = MutableStateFlow("")
    }

    open fun setUpSearchStateFlow(word: String, isOnline: Boolean): LiveData<T> =
        liveDataForViewToObserve

    abstract fun handleError(error: Throwable)


}
