package com.translator.view.main

import androidx.lifecycle.LiveData
import com.translator.domain.base.View
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.utils.parseSearchResults
import com.translator.viewmodel.BaseViewModel
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject


class MainViewModel @Inject constructor (private val interactor: MainInteractor)
    : BaseViewModel<AppState>() {

    private var appState: AppState? = null


    fun subscribe (): LiveData<AppState> {
        return liveDataForViewToObserve
    }



    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(doOnSubscribe())
                .subscribeWith(getObserver())
        )
        return super.getData(word, isOnline)
    }

//    override fun getData(word: String, isOnline: Boolean): LiveData<List<DataModel>> {
//        currentView?.showViewLoading()
//        compositeDisposable.add(
//            interactor.getData(word, isOnline)
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .doOnSubscribe { doOnSubscribe() }
//                .subscribeWith(getObserver())
//        )
//        return super.getData(word, isOnline)
//    }

//    private fun doOnSubscribe(): (Disposable) -> Unit =
//        { currentView?.showViewLoading() }
//
//
//    private fun getObserver(): DisposableObserver<List<DataModel>> {
//        return object : DisposableObserver<List<DataModel>>() {
//
//            override fun onNext(data: List<DataModel>) {
//                if (data.isEmpty()){
//                    currentView?.responseEmpty()
//                }else{
//                    liveDataForViewToObserve.value = data
//                }
//            }
//
//            override fun onError(e: Throwable) {
//                currentView?.showErrorScreen("Ошибка загрузки")
//            }
//
//            override fun onComplete() {
//            }
//
//
//        }
//
//    }
private fun doOnSubscribe(): (Disposable) -> Unit =
    { liveDataForViewToObserve.value = AppState.Loading(null) }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            override fun onNext(state: AppState) {
                appState = parseSearchResults(state)
                liveDataForViewToObserve.value = appState
            }

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
            }
        }
    }

}
