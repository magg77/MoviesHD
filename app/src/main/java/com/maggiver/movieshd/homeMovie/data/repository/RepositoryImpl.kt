package com.maggiver.movieshd.homeMovie.data.repository

import android.content.Context
import com.maggiver.movieshd.core.utils.ConnectionManager
import com.maggiver.movieshd.core.valueObject.ResourceNetwork
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.NowPlayingResponse
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.SearchMovieResponse
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.toMovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.toMovieSearchCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.server.DataSourceRemoteContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dataSourceRemote: DataSourceRemoteContract) :
    RepositoryContract {

    //get movies now playing
    override suspend fun getNowPlayingMovieRepo(reqireContext: Context): Flow<ResourceState<List<MovieCustom>>> =
        channelFlow {

            // Emitir estado de carga
            send(ResourceState.LoadingState())

            if (ConnectionManager.isNetworkAvailable(reqireContext)) {

                val response: ResourceState<NowPlayingResponse> =
                    dataSourceRemote.getNowPlayingMoviesRemoteContract()

                when (response) {

                    // Emitir éxito
                    is ResourceState.SuccessState -> {
                        val movies = response.data.results.map { it.toMovieCustom() }
                        send(ResourceState.SuccessState(movies)) // Emitir éxito
                    }

                    // Emitir error
                    is ResourceState.FailureState -> {
                        send(ResourceState.FailureState(response.message)) // Emitir error
                    }

                    else -> {}

                }

            } else {
                // Emitir error por red
                send(ResourceState.FailureState("No hay conexión de red"))
            }

        }



    //search movies real time
    override suspend fun getSearchMovieRepo(
        context: Context,
        query: String
    ): ResourceState<List<MovieSearchCustom>> {

        // Verificar la conexión de red
        if (!ConnectionManager.isNetworkAvailable(context)) {
            return ResourceState.FailureState("No hay conexión de red")
        }


        // Obtener la respuesta de la fuente remota
        return when (val response = dataSourceRemote.getSearchMovieRemoteContract(query)) {
            is ResourceState.SuccessState -> {
                val searchMovies = response.data.resultSearch.map { it.toMovieSearchCustom() }
                ResourceState.SuccessState(searchMovies)
            }
            is ResourceState.FailureState -> {
                ResourceState.FailureState(response.message)
            }
            else -> {
                ResourceState.FailureState("Error desconocido")
            }
        }

    }


}