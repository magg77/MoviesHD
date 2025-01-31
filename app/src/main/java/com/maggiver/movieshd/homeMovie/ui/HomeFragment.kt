package com.maggiver.movieshd.homeMovie.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.databinding.FragmentHomeBinding
import com.maggiver.movieshd.homeMovie.presentation.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModelPopularMovie by viewModels<NowPlayingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupObservers() {

        viewModelPopularMovie.nowPlayingMovies(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModelPopularMovie.uiState.collect(){

                    when(it){

                        is ResourceState.LoadingState -> {
                            Log.i("moviesnow", "Cargando data movies")
                        }

                        is ResourceState.SuccesState -> {
                            val data = it.data
                            Log.i("moviesnow", "Cargando data movies $data")
                        }

                        is ResourceState.FailureState -> {
                            Log.i("moviesnow", "Cargando data movies")
                        }

                        else -> {
                            Log.i("moviesnow", "Else when")
                        }
                    }

                }
            }
        }
    }




}