package com.maggiver.movieshd.homeMovie.data.provider.remote.server

import com.maggiver.movieshd.homeMovie.data.provider.remote.model.NowPlayingResponse
import org.intellij.lang.annotations.Language
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceContract {

    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("region") region: String = "ES"
    ): Response<NowPlayingResponse> // Usar Response<> para manejar la respuesta HTTP correctamente

}