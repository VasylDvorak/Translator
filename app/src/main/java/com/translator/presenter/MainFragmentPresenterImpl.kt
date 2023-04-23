package com.translator.presenter


import com.translator.model.data.AppState
import com.translator.model.datasource.DataSourceLocal
import com.translator.model.datasource.DataSourceRemote
import com.translator.model.repository.RepositoryImplementation
import com.translator.rx.SchedulerProvider
import com.translator.domain.base.View
import com.translator.domain.MainInteractor
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver

class MainFragmentPresenterImpl<T : AppState, V : View>(
    private val interactor: MainInteractor = MainInteractor(
        RepositoryImplementation(DataSourceRemote()),
        RepositoryImplementation(DataSourceLocal())
    ),
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : Presenter<T, V> {

    private var currentView: V? = null

    override fun attachView(view: V) {
        if (view != currentView) {
            currentView = view
        }
    }

    override fun detachView(view: V) {
        compositeDisposable.clear()
        if (view == currentView) {
            currentView = null
        }
    }

    override fun getData(word: String, isOnline: Boolean) {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { currentView?.appStateProgressEmpty() }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            override fun onNext(appState: AppState) {

                when (appState) {
                    is AppState.Success -> {
                        val dataModel = appState.data
                        if (dataModel == null || dataModel.isEmpty()) {

                            currentView?.responseEmpty()
                        } else {
                            currentView?.responseHasData(dataModel)
                        }
                    }
                    is AppState.Loading -> {
                        currentView?.showViewLoading()
                        if (appState.progress != null) {

                            currentView?.appStateProgressNotEmpty(appState.progress)

                        } else {
                            currentView?.appStateProgressEmpty()
                        }
                    }
                    is AppState.Error -> {
                        currentView?.showErrorScreen(appState.error.message)
                    }
                }

            }

            override fun onError(e: Throwable) {

                currentView?.showErrorScreen(AppState.Error(e).error.message)
            }

            override fun onComplete() {

            }
        }
    }
}
