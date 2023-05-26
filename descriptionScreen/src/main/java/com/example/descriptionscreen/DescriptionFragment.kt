package com.example.descriptionscreen

import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.descriptionscreen.databinding.FragmentDescriptionBinding
import com.example.model.data.DataModel
import com.example.utils.network.isOnline
import com.example.utils.ui.AlertDialogFragment
import org.koin.android.ext.android.getKoin
import java.io.IOException

const val CURRENT_DATA_MODEl = "current_data_model"
const val DIALOG_FRAGMENT_TAG = "dialog_fragment_tag"

class DescriptionFragment : Fragment() {
    var mMediaPlayer: MediaPlayer? = null
    private var _binding: FragmentDescriptionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.descriptionScreenSwipeRefreshLayout.setOnRefreshListener { startLoadingOrShowError() }
        setData()
    }


    private fun setData() {
        val currentDataModel =
            (arguments?.getParcelable(CURRENT_DATA_MODEl) as DataModel?) ?: DataModel()
        binding.descriptionHeader.text = currentDataModel.text
        if (currentDataModel.meanings?.size != 0) {
            binding.translationTextview.text =
                currentDataModel.meanings?.get(0)?.translation?.translation
            binding.transcription.text =
                "[" + currentDataModel.meanings?.get(0)?.transcription + "]"

            binding.playArticulation.setOnClickListener {
                currentDataModel.meanings?.get(0)?.soundUrl?.let { sound_url ->
                    playContentUrl(sound_url)
                }
            }
            val imageLink = currentDataModel.meanings?.get(0)?.imageUrl
            if (imageLink.isNullOrBlank()) {
                stopRefreshAnimationIfNeeded()
            } else {

                useGlideToLoadPhoto(binding.descriptionImageview, imageLink)

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

    private fun startLoadingOrShowError() {
        if (isOnline(getKoin().get())) {
            setData()
        } else {
            showNoInternetConnectionDialog()
            stopRefreshAnimationIfNeeded()
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        if (binding.descriptionScreenSwipeRefreshLayout.isRefreshing) {
            binding.descriptionScreenSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun useGlideToLoadPhoto(imageView: ImageView, imageLink: String) {
        Glide.with(this)
            .load("https:$imageLink")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    imageView.setImageResource(R.drawable.ic_load_error_vector)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_no_photo_vector)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .transform(CircleCrop())
            .into(imageView)
    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        activity?.let {
            AlertDialogFragment.newInstance(title, message)
                .show(it.supportFragmentManager, DIALOG_FRAGMENT_TAG)
        }
    }

    companion object {

        fun newInstance(bundle: Bundle): DescriptionFragment {
            val fragment = DescriptionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
