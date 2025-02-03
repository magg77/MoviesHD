package com.maggiver.movieshd.homeMovie.data.provider.remote.server

import com.maggiver.movieshd.homeMovie.data.provider.remote.model.NowPlayingResponse
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.SearchMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceContract {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("region") region: String = "ES"
    ): Response<NowPlayingResponse> // Usar Response<> para manejar la respuesta HTTP correctamente

    @GET("search/movie")
    suspend fun getSearchMovie(
        @Query("query") query: String = "es-ES",
        @Query("include_adult") include_adult: Boolean = false,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1
    ): Response<SearchMovieResponse>

}