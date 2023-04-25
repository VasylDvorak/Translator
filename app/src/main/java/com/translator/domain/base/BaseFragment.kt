package com.translator.domain.base

import androidx.fragment.app.Fragment
import com.translator.model.data.DataModel
import com.translator.viewmodel.BaseViewModel

abstract class BaseFragment<T : List<DataModel>> : Fragment(), View {

    abstract val model: BaseViewModel<T>

    abstract fun renderData(appState: T)

    override fun onStart() {
        super.onStart()
        model.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        model.detachView(this)
    }
}
