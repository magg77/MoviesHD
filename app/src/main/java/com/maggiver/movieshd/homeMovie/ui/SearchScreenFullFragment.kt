package com.maggiver.movieshd.homeMovie.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maggiver.movieshd.R
import com.maggiver.movieshd.core.utils.Constants
import com.maggiver.movieshd.databinding.FragmentDetailMovieFullScreenBinding
import com.maggiver.movieshd.databinding.FragmentSearchScreenFullBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchScreenFullFragment : DialogFragment() {

    private var _binding: FragmentSearchScreenFullBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentSearchScreenFullBinding.inflate(inflater, container, false)
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