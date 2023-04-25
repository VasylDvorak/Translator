package com.translator.domain.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.translator.R
import com.translator.application.App
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.utils.network.isOnline
import com.translator.utils.ui.AlertDialogFragment
import com.translator.viewmodel.BaseViewModel
import com.translator.viewmodel.Interactor

abstract class BaseFragment<T : AppState, I : Interactor<T>> : Fragment(), View {

    abstract val model: BaseViewModel<T>

    protected var isNetworkAvailable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkAvailable = isOnline(App.instance.applicationContext)
    }

    override fun onResume() {
        super.onResume()
        isNetworkAvailable = isOnline(App.instance.applicationContext)
        if (!isNetworkAvailable && isDialogNull()) {
            showNoInternetConnectionDialog()
        }
    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        activity?.let { AlertDialogFragment.newInstance(title, message)
            .show(it.supportFragmentManager, DIALOG_FRAGMENT_TAG) }
    }

    private fun isDialogNull(): Boolean {
        return activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }

    abstract fun renderData(dataModel: T)

    companion object {
        private const val DIALOG_FRAGMENT_TAG = "74a54328-5d62-46bf-ab6b-cbf5d8c79522"
    }
}
