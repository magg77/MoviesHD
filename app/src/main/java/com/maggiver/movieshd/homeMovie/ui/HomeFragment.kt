package com.maggiver.movieshd.homeMovie.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.maggiver.movieshd.core.valueObject.AdapterMovies
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.databinding.FragmentHomeBinding
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.presentation.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
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

        setupLayots()
        setupObservers()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //custom methods *******************************************************************************

    private fun setupLayots() {
        binding.rvHomeFragment.layoutManager = GridLayoutManager(
            requireContext(), 2, LinearLayoutManager.VERTICAL, false
        )
    }

    private fun setupObservers() {

        viewModelPopularMovie.nowPlayingMovies(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModelPopularMovie.uiState.collect(){

                    when(it){

                        is ResourceState.LoadingState -> {
                            binding.psHome.visibility = View.VISIBLE
                        }

                        is ResourceState.SuccesState -> {
                            binding.psHome.visibility = View.GONE

                            binding.rvHomeFragment.adapter =
                                AdapterMovies(
                                    context = requireContext(),
                                    moviesList = it.data,
                                    onItemClickListener = { dataResult ->
                                            Log.i("movieclick", "$dataResult")

                                        val action = HomeFragmentDirections.actionNavigationHomeToDetailMovieFragmentFullScreen(
                                            movieCustom = dataResult
                                        )
                                        findNavController().navigate(action)
                                    }
                                )

                        }

                        is ResourceState.FailureState -> {
                            binding.psHome.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Ocurrio un error al mostrar los datos: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
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