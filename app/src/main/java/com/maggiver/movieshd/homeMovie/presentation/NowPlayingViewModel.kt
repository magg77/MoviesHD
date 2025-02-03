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


    // LiveData que almacena el término de búsqueda actual, inicializado con "comedia".
    private val currentMovieSearch = savedStateHandle.getLiveData<String>("searchMovie", "comedia")

    private val _movieSearchResults = MutableLiveData<ResourceState<List<MovieSearchCustom>>>()
    val movieSearchResults: LiveData<ResourceState<List<MovieSearchCustom>>> = _movieSearchResults


    /**
     * Lanza una corrutina en el `viewModelScope` para obtener las películas en cartelera.
     *
     * @param context Contexto necesario para la ejecución del caso de uso.
     */
    fun nowPlayingMovies(context: Context) = viewModelScope.launch {
        usecase.invoke(context)
            .onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
    }

    /**
     * Actualiza el término de búsqueda de películas en tiempo real y lo guarda en el `savedStateHandle`.
     *
     * Esto permite que el estado de búsqueda persista si el ViewModel es recreado,
     * como en cambios de configuración (ej. rotación de pantalla).
     *
     * @param searchMovie Término de búsqueda ingresado por el usuario.
     */
    fun searchMovieRealTime(searchMovie: String) {
        currentMovieSearch.value = searchMovie
    }


    /**
     * Realiza una búsqueda de películas en tiempo real, emitiendo el estado de carga, éxito o error.
     *
     * La búsqueda se activa solo cuando el término cambia, utilizando `distinctUntilChanged`.
     * Se ejecuta en el contexto de IO para operaciones en segundo plano.
     *
     * @return Un `LiveData` que emite el estado de la operación de búsqueda, incluyendo los estados de carga, éxito o error.
     */
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