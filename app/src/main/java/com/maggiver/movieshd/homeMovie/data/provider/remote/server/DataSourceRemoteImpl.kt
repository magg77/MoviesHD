package com.maggiver.movieshd.homeMovie.data.provider.remote.server

import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.NowPlayingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class DataSourceRemoteImpl @Inject constructor(private val webServiceContract: WebServiceContract) :
    DataSourceRemoteContract {

    override suspend fun getNowPlayingMoviesRemoteContract(): ResourceState<NowPlayingResponse> {

        return withContext(Dispatchers.IO) {
            try {
                val response = webServiceContract.getNowPlayingMovies(
                    language = "es-ES", page = 1, region = "ES"
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ResourceState.SuccesState(body) // ✅ Éxito con datos
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
                ResourceState.FailureState("Error de red: ${e.message}") // ⚠️ Problemas de conectividad
            } catch (e: retrofit2.HttpException) {
                ResourceState.FailureState("Error HTTP: ${e.message}") // ⚠️ Respuesta HTTP fallida
            } catch (e: Exception) {
                ResourceState.FailureState("Error inesperado: ${e.message}") // ⚠️ Cualquier otro error
            }
        }

    }


}