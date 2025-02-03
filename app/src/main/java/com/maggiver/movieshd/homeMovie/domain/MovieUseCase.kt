package com.maggiver.movieshd.homeMovie.domain

import android.content.Context
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom
import com.maggiver.movieshd.homeMovie.data.repository.RepositoryContract
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val repo: RepositoryContract) :
    MovieUseCaseContract {


    override suspend fun invoke(requireContext: Context): Flow<ResourceState<List<MovieCustom>>> =
        repo.getNowPlayingMovieRepo(requireContext)


    override suspend fun getSearchMovieUseCase(
        context: Context,
        query: String
    ): ResourceState<List<MovieSearchCustom>> = repo.getSearchMovieRepo(context, query)


}