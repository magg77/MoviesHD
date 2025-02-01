package com.maggiver.movieshd.homeMovie.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maggiver.movieshd.R
import com.maggiver.movieshd.core.utils.Constants
import com.maggiver.movieshd.databinding.FragmentDetailMovieFullScreenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailMovieFragmentFullScreen : DialogFragment() {


    private var _binding: FragmentDetailMovieFullScreenBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailMovieFragmentFullScreenArgs>()

    private var backPressedCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        _binding = FragmentDetailMovieFullScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Remove the title bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Configura el ancho y alto del diálogo para que coincida con la pantalla
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitleMovieDetail.text = args.movieCustom.title
        Glide.with(requireContext())
            .load("${Constants.IMG_MOVIE_DB_COVER}${args.movieCustom.posterPath}")
            .apply(
                RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.baseline_add_a_photo_24)
                    .error(R.drawable.baseline_adb_24)
            )
            .into(binding.imageMovieDetail)

        binding.tvVote.text = "${args.movieCustom.voteCount}"
        binding.tvDateRelease.text = args.movieCustom.releaseDate
        binding.tvPopularity.text = "${args.movieCustom.popularity}"
        binding.tvDescripcionDetalle.text =
            args.movieCustom.overview.takeIf { it.isNotBlank() } ?: "Sin descripción disponible."


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isAdded) dismiss()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


    }

    override fun onResume() {
        super.onResume()

        val dispatcher = (dialog as? androidx.activity.ComponentDialog)?.onBackPressedDispatcher
        if (backPressedCallback == null) { // ✅ Solo lo registramos si aún no existe
            backPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dismiss()
                }
            }
            dispatcher?.addCallback(this, backPressedCallback!!)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}