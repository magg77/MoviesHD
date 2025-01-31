package com.maggiver.movieshd.homeMovie.domain

import android.content.Context
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import kotlinx.coroutines.flow.Flow

interface NowPlayingMovieUseCaseContract {

    suspend fun invoke(requireContext: Context): Flow<ResourceState<List<MovieCustom>>>

}