package com.maggiver.movieshd.homeMovie.domain

import android.content.Context
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom
import kotlinx.coroutines.flow.Flow

interface MovieUseCaseContract {

    suspend fun invoke(requireContext: Context): Flow<ResourceState<List<MovieCustom>>>


    suspend fun getSearchMovieUseCase(context: Context, query: String): ResourceState<List<MovieSearchCustom>>


}