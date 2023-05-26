package com.translator.view.main_fragment

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.IScreens
import com.example.core.base.BaseFragment
import com.github.terrakok.cicerone.Router
import com.google.gson.Gson
import com.translator.R
import com.translator.databinding.FragmentMainBinding
import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.utils.network.isOnline
import com.translator.view.BOTTOM_SHEET_FRAGMENT_DIALOG_TAG
import com.translator.view.SearchDialogFragment
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent

private const val LIST_KEY = "list_key"
class MainFragment : BaseFragment<AppState, MainInteractor>() {

    override lateinit var model: MainViewModel

    private val observer = Observer<AppState> { renderData(it) }
    private val observerFindWord = Observer<DataModel> { showWordInHistory(it) }



    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    private val router: Router by KoinJavaComponent.inject(Router::class.java)
   private val screen = KoinJavaComponent.getKoin().get<IScreens>()



    private val adapter: MainAdapter by lazy {

        MainAdapter(::onItemClick, ::putInFavorite, ::onPlayClick)
    }

    private fun putInFavorite(favorite: DataModel) {
model.putInFavorite(favorite)

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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        model.getRestoredData()?.let{ renderData(it) }

        val jsonStringList = activity?.getPreferences(Context.MODE_PRIVATE)?.getString(LIST_KEY, "")
        if (!jsonStringList.equals("")) {
            val listFromJson =
                Gson().fromJson(jsonStringList, Array<DataModel>::class.java).asList()
            updateAdapter(listFromJson)
        }

    }

    private fun findWordInHistory(){

            val searchDialogFragment = SearchDialogFragment.newInstance()
        searchDialogFragment.
            setOnSearchClickListener(object :
                SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    model.apply {
                        subscribeFindWord().observe(viewLifecycleOwner, observerFindWord)
                        findWordInHistory(searchWord)
                        subscribe().observe(viewLifecycleOwner, observer)
                    }

                }
            })

        searchDialogFragment.show(requireActivity().supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)


    }





    private fun showWordInHistory(findWord: DataModel) {
        if ((findWord.text=="")|| findWord.text.isNullOrBlank() || findWord.text.isNullOrEmpty()){
            showAlertDialog(
                getString(R.string.dialog_tittle_sorry),
                getString(R.string.no_word_in_history)
            )
        }else{
            findWord.let {
                router.navigateTo(screen.startDescriptionFragment(it))
            }
        }
    }

    private fun initViewModel() {
        if (binding.mainActivityRecyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        val viewModel: MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner, observer)
    }

    private fun initViews() {
            binding.mainActivityRecyclerview.layoutManager =
                LinearLayoutManager(context)
            binding.mainActivityRecyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        releaseMediaPlayer()
    }

    override fun responseEmpty() {

        showErrorScreen(getString(R.string.empty_server_response_on_success))
    }


    private fun updateAdapter(dataModel: List<DataModel>) {


        showViewSuccess()
        adapter?.setData(dataModel)
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

                    setDataToAdapter(data)
                }
            }

            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    binding.progressBarHorizontal.visibility = View.VISIBLE
                    binding.progressBarRound.visibility = View.GONE
                    binding.progressBarHorizontal.progress = appState.progress!!
                } else {
                    binding.progressBarHorizontal.visibility = View.GONE
                    binding.progressBarRound.visibility = View.VISIBLE
                }
            }

            is AppState.Error -> {
                showViewError()
                showAlertDialog(
                    getString(R.string.dialog_tittle_sorry),
                    getString(R.string.empty_server_response_on_success)
                )
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        searchItem.apply {

            searchView.also {

                it.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))
                it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {

                        isNetworkAvailable = isOnline(getKoin().get())
                        if (isNetworkAvailable) {

                            query?.let { searchString ->
                                model.getData(
                                    searchString,
                                    isNetworkAvailable
                                )
                            }
                            it.clearFocus()
                            it.setQuery("", false)
                            collapseActionView()
                            Toast.makeText(
                                context,
                                resources.getString(R.string.looking_for) + " " + query ?: "",
                                Toast.LENGTH_LONG
                            ).show()
                            showViewLoading()
                        } else {
                            showNoInternetConnectionDialog()
                            showViewError()
                        }
                        return true
                    }

                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }
        }

    }
    override fun onOptionsItemSelected (item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                router.navigateTo(screen.startHistoryFragment())
                true
            }
            R.id.find_word_in_history-> {
                findWordInHistory()
                true
            }
            R.id.favorite-> {
                router.navigateTo(screen.startFavoriteFragment())
                true
            }
            else -> super .onOptionsItemSelected(item)
        }
    }

    override fun showErrorScreen(error: String?) {

        showViewError()
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            model.getData("hi", true).observe(
                viewLifecycleOwner,
                observer
            )
        }
    }

    private fun showViewWorking() {
        binding.loadingFrameLayout.visibility = View.GONE
    }


    private fun showViewSuccess() {

        binding.successLinearLayout.visibility = View.VISIBLE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLinearLayout.visibility = View.GONE
    }

    override fun showViewLoading() {
        binding.successLinearLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.VISIBLE
        binding.errorLinearLayout.visibility = View.GONE
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        saveListForAdapter(data)
        showViewSuccess()
        adapter?.setData(data)
    }


    private fun showViewError() {
        binding.successLinearLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLinearLayout.visibility = View.VISIBLE
    }



    companion object {

        fun newInstance() = MainFragment()

    }
    }
