package com.translator.domain.base


import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.translator.R
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.utils.network.isOnline
import com.translator.utils.ui.AlertDialogFragment
import com.translator.viewmodel.BaseViewModel
import com.translator.viewmodel.Interactor
import org.koin.android.ext.android.getKoin
import java.io.IOException


abstract class BaseFragment<T : AppState, I : Interactor<T>> : Fragment(), ViewLayout {

    var mMediaPlayer: MediaPlayer? = null

    abstract val model: BaseViewModel<T>

    protected var isNetworkAvailable: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        isNetworkAvailable = isOnline(getKoin().get())
    }

    override fun onResume() {
        super.onResume()
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


    abstract fun setDataToAdapter (data : List <DataModel>)
    protected open fun renderData (appState: T) {

        when (appState) {
            is AppState.Success -> {
              //  showViewWorking()
                appState.data ?.let {
                    if (it.isEmpty()) {
                        showAlertDialog(
                            getString(R.string.dialog_tittle_sorry),
                            getString(R.string.empty_server_response_on_success)
                        )
                    } else {
                        setDataToAdapter(it)
                    }
                }
            }

            is AppState.Error -> {
                     showAlertDialog(getString(R.string.error_stub),
                    appState.error.message)
            }
        }
    }
    fun playContentUrl(url: String) {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.apply {
            try {
                setDataSource(url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setOnPreparedListener { start() }
                prepareAsync()
            } catch (exception: IOException) {
                release()
                null
            }
        }
    }

    fun releaseMediaPlayer(){
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }
    companion object {
        const val DIALOG_FRAGMENT_TAG = "dialog_fragment_tag"
    }
}
