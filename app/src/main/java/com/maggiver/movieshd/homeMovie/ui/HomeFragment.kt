package com.maggiver.movieshd.homeMovie.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.maggiver.movieshd.R
import com.maggiver.movieshd.core.valueObject.AdapterMovies
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.databinding.FragmentHomeBinding
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

        setupLayoutsMaterialToolbar()
        setupLayoutsAdapter()
        setupObservers()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //custom methods *******************************************************************************

    private fun setupLayoutsMaterialToolbar() {

        // this is compulsory in order to get behavior of expand/collapse
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar)

        setupMenu(R.menu.top_app_bar) { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    //Logica
                    Log.i("searchMoviesHD", "buscar movie click")

                    val action = HomeFragmentDirections.actionNavigationHomeToSearchMovieFragmentFullScreen()
                    findNavController().navigate(action)

                    true
                }

                else -> false

            }
        }

    }

    private fun Fragment.setupMenu(@MenuRes menuId: Int, onMenuSelected: (MenuItem) -> Boolean) =
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
                menuInflater.inflate(menuId, menu)

            override fun onMenuItemSelected(menuItem: MenuItem) = onMenuSelected(menuItem)
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    private fun setupLayoutsAdapter() {

        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = binding.rvHomeFragment.adapter as? AdapterMovies
                return if (adapter?.getItemViewType(position) == AdapterMovies.VIEW_TYPE_HEADER) 2 else 1
            }
        }
        binding.rvHomeFragment.setHasFixedSize(true)
        binding.rvHomeFragment.isNestedScrollingEnabled = false // Deshabilita el scrolling del RecyclerView dentro del NestedScrollView
        binding.rvHomeFragment.overScrollMode = View.OVER_SCROLL_NEVER
        binding.rvHomeFragment.layoutManager = layoutManager
    }

    private fun setupObservers() {

        viewModelPopularMovie.nowPlayingMovies(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelPopularMovie.uiState.collect() {

                    when (it) {

                        is ResourceState.LoadingState -> {
                            binding.psHome.visibility = View.VISIBLE
                        }

                        is ResourceState.SuccessState -> {
                            binding.psHome.visibility = View.GONE

                            binding.rvHomeFragment.adapter =
                                AdapterMovies(
                                    context = requireContext(),
                                    moviesList = listOf("Ver ahora") + it.data,
                                    onItemClickListener = { dataResult ->
                                        Log.i("movieclick", "$dataResult")

                                        val action =
                                            HomeFragmentDirections.actionNavigationHomeToDetailMovieFragmentFullScreen(
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