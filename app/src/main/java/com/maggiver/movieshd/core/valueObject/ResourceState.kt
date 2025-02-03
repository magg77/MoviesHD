package com.maggiver.movieshd.core.valueObject


sealed class ResourceState<out T> {

    class LoadingState<T> : ResourceState<T>()

    data class SuccessState<out T>(val data: T) : ResourceState<T>()

    data class FailureState(val message: String) : ResourceState<Nothing>()

}


sealed class ResourceNetwork<T> {
    class LoadingState<T>: ResourceState<T>()
    class SuccesState<T>(val data: T): ResourceState<T>()
    class FailureState<T>(val exception: Throwable): ResourceState<T>()
}

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T): ApiState<T>()
    data class Failure(val exception: Throwable): ApiState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Loading -> "Loading"
            is Success -> "Success $data"
            is Failure -> "Failure $exception"
        }
    }
}

sealed class Result<T> {
    data class Loading<T>(val isLoading: Boolean) : Result<T>()
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val errorMessage: String) : Result<T>()
}

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}