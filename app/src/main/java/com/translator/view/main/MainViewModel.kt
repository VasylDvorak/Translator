package com.translator.view.main

import androidx.lifecycle.LiveData
import com.translator.domain.MainInteractor
import com.translator.domain.base.View
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.datasource.DataSourceLocal
import com.translator.model.datasource.DataSourceRemote
import com.translator.model.repository.RepositoryImplementation
import com.translator.viewmodel.BaseViewModel
import io.reactivex.rxjava3.observers.DisposableObserver


class MainViewModel(
    private val interactor: MainInteractor = MainInteractor(
        RepositoryImplementation(DataSourceRemote()),
        RepositoryImplementation(DataSourceLocal())
    )
) : BaseViewModel<List<DataModel>>() {

    private var appState: AppState? = null

    private var currentView: View? = null

    override fun attachView(view: View) {
        if (view != currentView) {
            currentView = view
        }
    }

    override fun detachView(view: View) {
        compositeDisposable.clear()
        if (view == currentView) {
            currentView = null
        }
    }

    override fun getData(word: String, isOnline: Boolean): LiveData<List<DataModel>> {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { currentView?.showViewLoading() }
                .subscribeWith(getObserver())
        )
        return super.getData(word, isOnline)
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            override fun onNext(state: AppState) {

                when (state) {
                    is AppState.Success -> {
                        val dataModel = state.data
                        if (dataModel == null || dataModel.isEmpty()) {

                            currentView?.responseEmpty()
                        } else {
                            appState = state
                            liveDataForViewToObserve.value = state.data
                        }
                    }

                    is AppState.Loading -> {
                        currentView?.showViewLoading()
                        if (state.progress != null) {

                            currentView?.appStateProgressNotEmpty(state.progress)

                        } else {
                            currentView?.appStateProgressEmpty()
                        }
                    }
                    is AppState.Error -> {
                        currentView?.showErrorScreen(state.error.message)
                    }

                }

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {
            }


        }


    }


}
