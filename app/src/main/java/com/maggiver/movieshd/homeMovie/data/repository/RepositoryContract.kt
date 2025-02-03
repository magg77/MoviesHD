package com.maggiver.movieshd.homeMovie.data.repository

import android.content.Context
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.SearchMovieResponse
import kotlinx.coroutines.flow.Flow

interface RepositoryContract {

    //remote now playing
    suspend fun getNowPlayingMovieRepo(reqireContext: Context): Flow<ResourceState<List<MovieCustom>>>

    //remote search movies
    suspend fun getSearchMovieRepo(context: Context, query: String): ResourceState<List<MovieSearchCustom>>



}