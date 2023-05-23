package com.translator.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import com.github.terrakok.cicerone.Router
import com.translator.R
import com.translator.databinding.FragmentHistoryFavoriteBinding

import com.translator.domain.base.BaseFragment
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.navigation.IScreens
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent


class FavoriteFragment : BaseFragment<AppState, FavoriteInteractor>() {


    private var _binding: FragmentHistoryFavoriteBinding? =null
    private val binding
        get() = _binding!!

    private val router: Router by KoinJavaComponent.inject(Router::class.java)
    private val screen = KoinJavaComponent.getKoin().get<IScreens>()

    override lateinit var model: FavoriteViewModel
    private val adapter: FavoriteAdapter by lazy { FavoriteAdapter(::onItemClick, ::onPlayClick, ::onRemove) }

    private fun onRemove(i: Int, dataModel: DataModel) {
        model.remove(dataModel)
        model.subscribe().observe(viewLifecycleOwner) {appState ->
            when (appState) {
                is AppState.Success -> {
                    //  showViewWorking()
                    appState.data?.let {
                        if (it.size !=0) {
                            println("************* "+it.size)
                            renderData(appState)
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun onItemClick(dataModel: DataModel) {
        dataModel.let {
            router.navigateTo(screen.startDescriptionFragment(it))
        }
    }
    private fun onPlayClick(url: String) {
        playContentUrl(url)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViewModel()
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        releaseMediaPlayer()
    }

    override fun responseEmpty() {}

    override fun showViewLoading() {}

    override fun showErrorScreen(error: String?) {}


    override fun onResume() {
        super.onResume()
        model.getData("", false)
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }


    private fun iniViewModel() {
        if (binding.historyActivityRecyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        val viewModel: FavoriteViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Success -> {
                    appState.data?.let {
                        if (it.size !=0) {
                            renderData(appState)
                        }
                    }
                }

                else -> {}
            }


             }
    }

    private fun initViews() {
        binding.apply {
            history.text=getString(R.string.favorites)
            historyActivityRecyclerview.adapter = adapter
            ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(historyActivityRecyclerview)
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
