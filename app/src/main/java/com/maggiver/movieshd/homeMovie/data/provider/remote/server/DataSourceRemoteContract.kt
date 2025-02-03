package com.maggiver.movieshd.homeMovie.data.provider.remote.server
import com.google.gson.annotations.SerializedName
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.NowPlayingResponse
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.SearchMovieResponse


interface DataSourceRemoteContract {

   suspend fun getNowPlayingMoviesRemoteContract(): ResourceState<NowPlayingResponse>

   suspend fun getSearchMovieRemoteContract(query: String): ResourceState<SearchMovieResponse>

}