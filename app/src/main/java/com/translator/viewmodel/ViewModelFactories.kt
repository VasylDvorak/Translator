package com.translator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.translator.view.history.HistoryInteractor
import com.translator.view.history.HistoryViewModel
import com.translator.view.main_fragment.MainInteractor
import com.translator.view.main_fragment.MainViewModel

class MainViewModelFactory constructor (
    var interactor: MainInteractor
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  MainViewModel(interactor) as T
    }
}

class HistoryViewModelFactory constructor (
    var interactor: HistoryInteractor
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  HistoryViewModel(interactor) as T
    }
}


