package com.maggiver.movieshd.homeMovie.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maggiver.movieshd.core.valueObject.MoviesHdApplication
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.domain.NowPlayingMovieUseCaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(private val usecase: NowPlayingMovieUseCaseContract) :
    ViewModel() {

    @get: Inject
    val baseApplication: MoviesHdApplication
        get() {
            return MoviesHdApplication()
        }

    private val _uiState: MutableStateFlow<ResourceState<List<MovieCustom>>> =
        MutableStateFlow(ResourceState.LoadingState())
    val uiState: StateFlow<ResourceState<List<MovieCustom>>> = _uiState

    fun nowPlayingMovies(context: Context) = viewModelScope.launch {
        usecase.invoke(context)
            .onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
    }


}