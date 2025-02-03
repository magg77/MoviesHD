package com.maggiver.movieshd.homeMovie.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.maggiver.movieshd.core.valueObject.MoviesHdApplication
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom
import com.maggiver.movieshd.homeMovie.domain.MovieUseCaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val usecase: MovieUseCaseContract,
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    //now playing
    private val _uiState: MutableStateFlow<ResourceState<List<MovieCustom>>> =
        MutableStateFlow(ResourceState.LoadingState())
    val uiState: StateFlow<ResourceState<List<MovieCustom>>> = _uiState


    //search movies
    private val currentMovieSearch = savedStateHandle.getLiveData<String>("searchMovie", "comedia")
    private val _movieSearchResults = MutableLiveData<ResourceState<List<MovieSearchCustom>>>()
    val movieSearchResults: LiveData<ResourceState<List<MovieSearchCustom>>> = _movieSearchResults


    // ultimas peliculas en cines
    fun nowPlayingMovies(context: Context) = viewModelScope.launch {
        usecase.invoke(context)
            .onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
    }


    // búsqueda de películas en tiempo real
    fun fetchMovieSearch() =
        currentMovieSearch.distinctUntilChanged().switchMap { search ->
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {

                emit(ResourceState.LoadingState())

                try {

                    emit(usecase.getSearchMovieUseCase(context, search))
                }catch (e: Exception){
                    emit(ResourceState.FailureState(e.message!!))
                }
            }


        }


}