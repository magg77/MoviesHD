package com.maggiver.movieshd.core.di

import com.maggiver.movieshd.homeMovie.data.provider.remote.server.DataSourceRemoteContract
import com.maggiver.movieshd.homeMovie.data.provider.remote.server.DataSourceRemoteImpl
import com.maggiver.movieshd.homeMovie.data.repository.RepositoryContract
import com.maggiver.movieshd.homeMovie.data.repository.RepositoryImpl
import com.maggiver.movieshd.homeMovie.domain.NowPlayingMovieUseCase
import com.maggiver.movieshd.homeMovie.domain.NowPlayingMovieUseCaseContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindContractsModule {

    @Binds
    abstract fun nowPlayingMovieUseCase(userCaseNowPlayingMovie: NowPlayingMovieUseCase): NowPlayingMovieUseCaseContract

    @Binds
    abstract fun bindRepo(repo: RepositoryImpl): RepositoryContract

    @Binds
    abstract fun dataSourceRemote(dataRemote: DataSourceRemoteImpl): DataSourceRemoteContract


}