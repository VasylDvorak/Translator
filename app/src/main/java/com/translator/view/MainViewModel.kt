package com.translator.view

import androidx.lifecycle.LiveData
import com.translator.model.data.AppState
import com.translator.utils.parseSearchResults
import com.translator.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val QUERY = "query"

class MainViewModel (private val interactor: MainInteractor) :
    BaseViewModel<AppState>() {


    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    fun getRestoredData(): AppState? = savedStateHandle[QUERY]
    fun setQuery(query: AppState) {
        savedStateHandle[QUERY] = query
    }

    private suspend fun startInteractor (word: String, isOnline: Boolean ) {
        liveDataForViewToObserve.postValue(
            parseSearchResults(
                interactor.getData(
                    word,
                    isOnline
                )
            )
        )
    }



    // Обрабатываем ошибки
    override fun handleError (error: Throwable ) {
        liveDataForViewToObserve.postValue(AppState.Error(error))
    }
    override fun onCleared () {
        liveDataForViewToObserve.value = AppState.Success( null )
        super .onCleared()
    }

    override fun setUpSearchStateFlow(word: String, isOnline: Boolean): LiveData<AppState>  {
        cancelJob()
        queryStateFlow.value=word
        CoroutineScope(Dispatchers.Main + job).launch {
            queryStateFlow.filter { query ->
                    if (query.isEmpty()) {
                        liveDataForViewToObserve.postValue(AppState.Error(Throwable("Пустая строка")))
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emit("")
                        }
                }
                .collect { result ->
                    startInteractor(result, isOnline) }
        }
        return super.setUpSearchStateFlow(word, isOnline)
    }

    private fun dataFromNetwork(query: String): Flow<String> {
        return flow {
            emit(query)
        }
    }


    }


