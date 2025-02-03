package com.maggiver.movieshd.homeMovie.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maggiver.movieshd.R
import com.maggiver.movieshd.core.utils.hide
import com.maggiver.movieshd.core.utils.show
import com.maggiver.movieshd.core.utils.showIf
import com.maggiver.movieshd.core.utils.showToast
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.core.valueObject.SearchAdapter
import com.maggiver.movieshd.databinding.FragmentSearchMovieFullScreenBinding
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.toMovieCustom
import com.maggiver.movieshd.homeMovie.presentation.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieFragmentFullScreen : DialogFragment(), SearchAdapter.OnMovieSearchClickListener {

    private var _binding: FragmentSearchMovieFullScreenBinding? = null
    private val binding get() = _binding!!

    private var backPressedCallback: OnBackPressedCallback? = null

    private val viewModelSearchMovie by viewModels<NowPlayingViewModel>()

    private lateinit var searchAdapter: SearchAdapter

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
        _binding = FragmentSearchMovieFullScreenBinding.inflate(inflater, container, false)
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

        setupLayuotToolbar()
        setupLayuotSearchRV()
        setupObserverSearch()


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

    //custom methods *******************************************************************************

    private fun setupLayuotToolbar() {

        //focus searchView
        binding.searchMovie.onActionViewExpanded()

        // Se asegura de que el teclado se muestre
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchMovie, InputMethodManager.SHOW_IMPLICIT)

        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                newText?.let {
                    Log.i("resultSearch", "data $it")
                    viewModelSearchMovie.searchMovieRealTime(requireContext(), it)
                }

                return true
            }

        })
    }

    private fun setupLayuotSearchRV() {
        searchAdapter = SearchAdapter(requireContext(), this)
        binding.rvMoviesSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoviesSearch.adapter = searchAdapter
    }

    private fun setupObserverSearch() {
        viewModelSearchMovie.fetchMovieSearch().observe(viewLifecycleOwner, Observer { result ->

            binding.progressBar.showIf { result is ResourceState.LoadingState }

            when (result) {

                is ResourceState.LoadingState -> {
                    binding.emptyContainer.root.hide()
                }

                is ResourceState.SuccessState -> {
                    if (result.data.isEmpty()) {
                        binding.rvMoviesSearch.hide()
                        binding.emptyContainer.root.show()
                        return@Observer
                    }
                    binding.rvMoviesSearch.show()
                    searchAdapter.setMovielList(result.data)
                    binding.emptyContainer.root.hide()
                }

                is ResourceState.FailureState -> {
                    Log.i("resultSearch", "error: ${result.message}")
                    showToast("Ocurrió un error al traer los datos ${result.message}")
                }

                else -> {}

            }

        })
    }

    override fun onMovieSearchCustom(movieSearchCustom: MovieSearchCustom, position: Int) {
        findNavController().navigate(
            SearchMovieFragmentFullScreenDirections.actionSearchMovieFragmentFullScreenToDetailMovieFragmentFullScreen(
                movieCustom = movieSearchCustom.toMovieCustom()
            )
        )
    }

}