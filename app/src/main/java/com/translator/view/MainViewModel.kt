package com.translator.view

import androidx.lifecycle.LiveData
import com.translator.model.data.AppState
import com.translator.utils.parseSearchResults
import com.translator.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val QUERY = "query"

class MainViewModel (private val interactor: MainInteractor) :
    BaseViewModel<AppState>() {

    private var appState: AppState? = null


    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    fun getRestoredData(): AppState? = savedStateHandle[QUERY]
    fun setQuery(query: AppState) {
        savedStateHandle[QUERY] = query
    }

    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {
        liveDataForViewToObserve.value = AppState.Loading( null )
        cancelJob()
// Запускаем корутину для асинхронного доступа к серверу с помощью
// launch
        viewModelCoroutineScope.launch { startInteractor(word, isOnline) }
        return super.getData(word, isOnline)
    }

    private suspend fun startInteractor (word: String , isOnline: Boolean ) =
        withContext(Dispatchers.IO) {
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



    }


