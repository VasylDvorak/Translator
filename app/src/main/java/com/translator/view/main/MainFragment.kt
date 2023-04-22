package com.translator.view.main



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.translator.R
import com.translator.databinding.FragmentMainBinding
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.presenter.Presenter
import com.translator.view.base.BaseFragment
import com.translator.view.base.View
import com.translator.view.main.adapter.MainAdapter


class MainFragment : BaseFragment<AppState>() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    private var adapter: MainAdapter? = null
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(context, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    override fun createPresenter(): Presenter<AppState, View> {
        return MainFragmentPresenterImpl()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchListener()
    }


override fun searchListener(){
    binding.apply {

        inputLayout.setEndIconOnClickListener {

            var searchWord:String? = inputEditText.text.toString()
            presenter.getData(searchWord!!, true)
            inputEditText.text=null
            wikipediaMotion.transitionToStart()
            ViewCompat.getWindowInsetsController(requireView())?.hide(WindowInsetsCompat.Type.ime())
        }

    }

}


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun responseEmpty() {

        showErrorScreen(getString(R.string.empty_server_response_on_success))
    }

    override fun responseHasData(dataModel: List<DataModel>) {

        showViewSuccess()

        if (adapter == null) {
            binding.mainActivityRecyclerview.layoutManager =
                LinearLayoutManager(context)
            binding.mainActivityRecyclerview.adapter =
                MainAdapter(onListItemClickListener, dataModel)
        } else {
            adapter!!.setData(dataModel)
        }
    }


    override fun showErrorScreen(error: String?) {

        showViewError()
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            presenter.getData("hi", true)
        }
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

    override fun appStateProgressNotEmpty(progress: Int) {
        binding.successLinearLayout.visibility = GONE
        binding.progressBarHorizontal.visibility = VISIBLE
        binding.progressBarRound.visibility = GONE
        binding.progressBarHorizontal.progress = progress
    }

    override fun appStateProgressEmpty() {
        binding.successLinearLayout.visibility = GONE
        binding.progressBarHorizontal.visibility = GONE
        binding.progressBarRound.visibility = VISIBLE
    }

    private fun showViewError() {
        binding.successLinearLayout.visibility = GONE
        binding.loadingFrameLayout.visibility = GONE
        binding.errorLinearLayout.visibility = VISIBLE
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
