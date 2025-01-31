package com.maggiver.movieshd.homeMovie.data.repository

import android.content.Context
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import kotlinx.coroutines.flow.Flow

interface RepositoryContract {

    //Remote
    suspend fun repoGetNowPlayingMovie(reqireContext: Context): Flow<ResourceState<List<MovieCustom>>>


}