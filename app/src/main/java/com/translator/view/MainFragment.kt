package com.translator.view


import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.translator.R
import com.translator.application.App
import com.translator.databinding.FragmentMainBinding
import com.translator.domain.base.BaseFragment
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.utils.network.isOnline
import com.translator.view.adapter.MainAdapter
import java.io.IOException
import javax.inject.Inject


private const val LIST_KEY = "list_key"

class MainFragment : BaseFragment<AppState, MainInteractor>() {


    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    override lateinit var model: MainViewModel

    private val observer = Observer<AppState> { renderData(it) }

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    var mMediaPlayer: MediaPlayer? = null

    private var adapter: MainAdapter? = null
    private var savedDataModel: MutableList<DataModel> = mutableListOf()

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(context, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    private val playArticulationClickListener: MainAdapter.OnPlayArticulationClickListener =
        object : MainAdapter.OnPlayArticulationClickListener {
            override fun onPlayClick(url: String) {
                playContentUrl(url)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        model = viewModelFactory.create(MainViewModel::class.java)
        model.subscribe().observe(viewLifecycleOwner, observer)
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveListForAdapter(savedDataModel)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)



        model.getRestoredData()?.let { renderData(it) }

        val jsonStringList = activity?.getPreferences(Context.MODE_PRIVATE)?.getString(LIST_KEY, "")
        if (!jsonStringList.equals("")) {
            val ListFromJson =
                Gson().fromJson(jsonStringList, Array<DataModel>::class.java).asList()
                updateAdapter(ListFromJson)
        }

        searchListener()
    }


    override fun searchListener() {
        binding.apply {

            inputLayout.setEndIconOnClickListener {

                var searchWord: String = inputEditText.text.toString() ?: ""


                isNetworkAvailable = isOnline(App.instance.applicationContext)
                if (isNetworkAvailable) {
                    model.getData(searchWord, isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }

                inputEditText.text = null
                searchMotion.transitionToStart()
                ViewCompat.getWindowInsetsController(requireView())
                    ?.hide(WindowInsetsCompat.Type.ime())
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }

    override fun responseEmpty() {

        showErrorScreen(getString(com.translator.R.string.empty_server_response_on_success))
    }


    private fun updateAdapter(dataModel: List<DataModel>) {
        savedDataModel = dataModel.toMutableList()
        showViewSuccess()

        if (adapter == null) {

            binding.mainActivityRecyclerview.layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(onListItemClickListener, playArticulationClickListener, dataModel)
            binding.mainActivityRecyclerview.adapter=adapter

        } else {
            adapter?.setData(dataModel)
        }
    }


    override fun renderData(appState: AppState) {
        model.setQuery(appState)
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showAlertDialog(
                        getString(R.string.dialog_tittle_sorry),
                        getString(R.string.empty_server_response_on_success)
                    )
                } else {

                    updateAdapter(data)
                }
            }

            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    binding.progressBarHorizontal.visibility = VISIBLE
                    binding.progressBarRound.visibility = GONE
                    binding.progressBarHorizontal.progress = appState.progress
                } else {
                    binding.progressBarHorizontal.visibility = GONE
                    binding.progressBarRound.visibility = VISIBLE
                }
            }

            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }


    private fun saveListForAdapter(dataModel: List<DataModel>) {

        var jsonStr = Gson().toJson(dataModel)

        with(activity?.getPreferences(Context.MODE_PRIVATE)?.edit()) {
            this?.putString(LIST_KEY, jsonStr)
            this?.apply()
        }
    }


    override fun showErrorScreen(error: String?) {

        showViewError()
        binding.errorTextview.text = error ?: getString(com.translator.R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            model.getData("hi", true).observe(
                viewLifecycleOwner,
                observer
            )
        }
    }

    private fun showViewWorking() {
        binding.loadingFrameLayout.visibility = GONE
    }


    private fun showViewSuccess() {

        binding.successLinearLayout.visibility = VISIBLE
        binding.loadingFrameLayout.visibility = GONE
        binding.errorLinearLayout.visibility = GONE
    }

    override fun showViewLoading() {
        binding.successLinearLayout.visibility = GONE
        binding.loadingFrameLayout.visibility = VISIBLE
        binding.errorLinearLayout.visibility = GONE
    }


    private fun showViewError() {
        binding.successLinearLayout.visibility = GONE
        binding.loadingFrameLayout.visibility = GONE
        binding.errorLinearLayout.visibility = VISIBLE
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


    companion object {
        fun newInstance() = MainFragment()
    }
}
