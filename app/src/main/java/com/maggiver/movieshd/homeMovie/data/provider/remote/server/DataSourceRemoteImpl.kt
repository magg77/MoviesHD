package com.maggiver.movieshd.homeMovie.data.provider.remote.server

import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.NowPlayingResponse
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.SearchMovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class DataSourceRemoteImpl @Inject constructor(private val webServiceContract: WebServiceContract) :
    DataSourceRemoteContract {


    /**
     * Realiza una solicitud remota para obtener las películas en cartelera (Now Playing).
     *
     * La solicitud se ejecuta en un hilo IO para no bloquear el hilo principal. Se verifica la respuesta
     * del servidor y se emite un estado de éxito o error según corresponda.
     *
     * @return Un `ResourceState` que contiene el resultado de la operación de búsqueda o un mensaje de error.
     */
    override suspend fun getNowPlayingMoviesRemoteContract(): ResourceState<NowPlayingResponse> {

        return withContext(Dispatchers.IO) {

            try {

                val response = webServiceContract.getNowPlayingMovies(
                    language = "es-ES", page = 1, region = "ES"
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ResourceState.SuccessState(body) // ✅ Éxito con datos
                    } else {
                        ResourceState.FailureState("Respuesta vacía del servidor") // ⚠️ Cuerpo nulo
                    }
                } else {
                    // ⚠️ Manejar códigos de error HTTP específicos
                    val errorMessage = when (response.code()) {
                        400 -> "Solicitud incorrecta (Bad Request)"
                        401 -> "No autorizado (Unauthorized)"
                        403 -> "Prohibido (Forbidden)"
                        404 -> "Recurso no encontrado (Not Found)"
                        500 -> "Error interno del servidor"
                        else -> "Error desconocido: ${response.code()}"
                    }
                    ResourceState.FailureState(errorMessage)
                }
            } catch (e: IOException) {

                // ⚠️ Problemas de conectividad
                ResourceState.FailureState("Error de red: ${e.message}")

            } catch (e: retrofit2.HttpException) {

                // ⚠️ Respuesta HTTP fallida
                ResourceState.FailureState("Error HTTP: ${e.message}")

            } catch (e: Exception) {

                // ⚠️ Cualquier otro error
                ResourceState.FailureState("Error inesperado: ${e.message}")

            }
        }

    }


    /**
     * Realiza una solicitud remota para buscar una película usando el servicio web.
     *
     * Se ejecuta de manera suspendida en un contexto IO para manejar la solicitud en segundo plano.
     * La respuesta es procesada y emitida como un estado que indica si la operación fue exitosa o fallida.
     *
     * @param query Término de búsqueda ingresado por el usuario.
     * @return Un `ResourceState` que contiene el resultado de la búsqueda o un mensaje de error.
     */
    override suspend fun getSearchMovieRemoteContract(query: String): ResourceState<SearchMovieResponse> {
        return withContext(Dispatchers.IO) {

            try {

                val response = webServiceContract.getSearchMovie(
                    query = query,
                    include_adult = false,
                    language = "es-ES",
                    page = 1
                )

                // Verificar si la respuesta fue exitosa
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ResourceState.SuccessState(body) // ✅ Éxito con datos
                    } else {
                        ResourceState.FailureState("Respuesta vacía del servidor") // ⚠️ Cuerpo nulo
                    }
                } else {
                    // Manejar errores HTTP específicos
                    val errorMessage = when (response.code()) {
                        400 -> "Solicitud incorrecta (Bad Request)"
                        401 -> "No autorizado (Unauthorized)"
                        403 -> "Prohibido (Forbidden)"
                        404 -> "Recurso no encontrado (Not Found)"
                        500 -> "Error interno del servidor"
                        else -> "Error desconocido: ${response.code()}"
                    }
                    ResourceState.FailureState(errorMessage)
                }


            } catch (e: IOException) {

                // ⚠️ Problemas de conectividad
                ResourceState.FailureState("Error de red: ${e.message ?: "Desconocido IOException"}")

            } catch (e: retrofit2.HttpException) {

                // ⚠️ Respuesta HTTP fallida
                ResourceState.FailureState("Error HTTP: ${e.message ?: "Desconocido HttpException"}")

            } catch (e: Exception) {

                // ⚠️ Cualquier otro error
                ResourceState.FailureState("Error inesperado: ${e.message ?: "Desconocido Exception"}")

            }
        }
    }


}