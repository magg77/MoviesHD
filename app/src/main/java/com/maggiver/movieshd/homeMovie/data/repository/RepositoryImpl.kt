package com.maggiver.movieshd.homeMovie.data.repository

import android.content.Context
import com.maggiver.movieshd.core.utils.ConnectionManager
import com.maggiver.movieshd.core.valueObject.ResourceNetwork
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.NowPlayingResponse
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.toMovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.server.DataSourceRemoteContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dataSourceRemote: DataSourceRemoteContract) :
    RepositoryContract {


    override suspend fun repoGetNowPlayingMovie(reqireContext: Context): Flow<ResourceState<List<MovieCustom>>> =
        channelFlow {

            // Emitir estado de carga
            send(ResourceState.LoadingState())

            if (ConnectionManager.isNetworkAvailable(reqireContext)) {

                val response: ResourceState<NowPlayingResponse> =
                    dataSourceRemote.getNowPlayingMoviesRemoteContract()

                when (response) {

                    // Emitir éxito
                    is ResourceState.SuccesState -> {
                        val movies = response.data.results.map { it.toMovieCustom() }
                        send(ResourceState.SuccesState(movies)) // Emitir éxito
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


}