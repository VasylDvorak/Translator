package com.translator.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.translator.R
import com.translator.databinding.FragmentHistoryFavoriteBinding
import com.translator.di.ConnectKoinModules.favoriteScreenScope
import com.translator.domain.base.BaseFragment
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.navigation.IScreens
import com.translator.utils.ui.viewById
import org.koin.java.KoinJavaComponent


class FavoriteFragment : BaseFragment<AppState, FavoriteInteractor>() {


    private var _binding: FragmentHistoryFavoriteBinding? = null
    private val binding
        get() = _binding!!

    private val router: Router by KoinJavaComponent.inject(Router::class.java)
    private val screen = KoinJavaComponent.getKoin().get<IScreens>()
    private val historyActivityRecyclerview by viewById<RecyclerView>(R.id.history_activity_recyclerview)
    override lateinit var model: FavoriteViewModel
    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            ::onItemClick,
            ::onPlayClick,
            ::onRemove
        )
    }

    private fun onRemove(i: Int, dataModel: DataModel) {
        model.remove(dataModel)
        model.subscribe().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Success -> {
                    appState.data?.let {
                        if (it.size != 0) {
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


    override fun onResume() {
        super.onResume()
        model.getData("", false)
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }


    private fun iniViewModel() {
        if (historyActivityRecyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }

        val viewModel: FavoriteViewModel by lazy { favoriteScreenScope.get() }
        model = viewModel


        model.subscribe().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Success -> {
                    appState.data?.let {
                        if (it.size != 0) {
                            renderData(appState)
                        }
                    }
                }

                else -> {}
            }


        }
    }

    private fun initViews() {
        binding.history.text = getString(R.string.favorites)
        historyActivityRecyclerview.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(
            historyActivityRecyclerview
        )
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
