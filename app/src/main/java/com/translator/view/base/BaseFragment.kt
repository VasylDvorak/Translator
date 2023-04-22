package com.translator.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.translator.model.data.AppState
import com.translator.presenter.Presenter

abstract class BaseFragment<T : AppState> : Fragment(), View {

    protected lateinit var presenter: Presenter<T, View>

    protected abstract fun createPresenter(): Presenter<T, View>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView(this)
    }
}
