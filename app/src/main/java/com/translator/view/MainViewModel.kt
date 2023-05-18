package com.translator.view

import androidx.lifecycle.LiveData
import com.translator.model.data.AppState
import com.translator.utils.parseSearchResults
import com.translator.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val QUERY = "query"

class MainViewModel(private val interactor: MainInteractor) :
    BaseViewModel<AppState>() {


    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    fun getRestoredData(): AppState? = savedStateHandle[QUERY]
    fun setQuery(query: AppState) {
        savedStateHandle[QUERY] = query
    }

    // Обрабатываем ошибки
    override fun handleError(error: Throwable) {
        liveDataForViewToObserve.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        liveDataForViewToObserve.value = AppState.Success(null)
        super.onCleared()
    }

    override fun setUpSearchStateFlow(word: String, isOnline: Boolean): LiveData<AppState> {
        queryStateFlow.value = Pair(word, isOnline)
        coroutineScope.launch {
            queryStateFlow.filter { query ->
                if (query.first.isEmpty()) {
                    liveDataForViewToObserve.postValue(AppState.Error(Throwable("Пустая строка")))
                    return@filter false
                } else {
                    return@filter true
                }
            }
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emit(AppState.Error(Throwable("")))
                        }
                }
                .filterNotNull()
                .collect { result ->
                    liveDataForViewToObserve.postValue(result)
                }
        }

        return super.setUpSearchStateFlow(word, isOnline)
    }

    private fun dataFromNetwork(query: Pair<String, Boolean>): Flow<AppState> {
        return flow {
            emit(
                parseSearchResults(
                    interactor.getData(
                        query.first,
                        query.second
                    )
                )
            )
        }
    }

}


