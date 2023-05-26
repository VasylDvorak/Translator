package com.example.historyscreen

import androidx.lifecycle.LiveData
import com.example.core.viewmodel.BaseViewModel
import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.utils.parseLocalSearchResults
import com.example.utils.parseWordSearchResults

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class HistoryViewModel(private val interactor: HistoryInteractor) :
    BaseViewModel<AppState>() {

    override var liveDataForViewToObserve = _mutableLiveData

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    fun subscribeFindWord(): LiveData<DataModel> {
        return liveDataFindWordInHistory
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

    fun putInFavorite(favorite: DataModel) {
        coroutineScope.launch {
            interactor.putFavorite(
                favorite
            )
        }
    }

    override fun findWordInHistory(word: String): LiveData<DataModel> {
        queryStateFlowFindWordFromHistory.value = word
        coroutineScope.launch {
            queryStateFlowFindWordFromHistory.filter { query ->
                if (query.isEmpty() || (query == "")) {
                    liveDataFindWordInHistory.postValue(DataModel())
                    return@filter false
                } else {
                    return@filter true
                }
            }
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    findWordFromHistory(query)
                        .catch {
                            emit(DataModel())
                        }
                }
                .filterNotNull()
                .collect { result ->
                    liveDataFindWordInHistory.postValue(result)
                }
        }
        return super.findWordInHistory(word)
    }

    private fun findWordFromHistory(query: String): Flow<DataModel> {
        return flow {
            emit(
                parseWordSearchResults(
                    interactor.getWordFromDB(
                        query
                    )
                )
            )
        }
    }
}
