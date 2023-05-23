package com.translator.view.favorite

import androidx.lifecycle.LiveData
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.utils.parseLocalSearchResults
import com.translator.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteViewModel(private val interactor: FavoriteInteractor) :
    BaseViewModel<AppState>() {

    override var liveDataForViewToObserve = _mutableLiveData

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {
        _mutableLiveData.value = AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch { startInteractor(word, isOnline) }
        return super.getData(word, isOnline)
    }

    private suspend fun startInteractor(word: String, isOnline: Boolean) {
        _mutableLiveData.postValue(parseLocalSearchResults(interactor.getData(word, isOnline)))
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = AppState.Success(null)
        super.onCleared()
    }

    fun remove(dataModel: DataModel) {
        coroutineScope.launch {
            interactor.removeFavoriteItem(
                dataModel
            )
            _mutableLiveData.postValue(parseLocalSearchResults(interactor.getData("", true)))
        }
    }
}
