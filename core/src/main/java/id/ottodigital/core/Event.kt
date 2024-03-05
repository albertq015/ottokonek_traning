package id.ottodigital.core

sealed class Event<T> {
    data class Loading<T>(var loading: Boolean) : Event<T>()
    data class Success<T>(var result: T) : Event<T>()
    data class Failure<T>(var message: String) : Event<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): Event<T> = Loading(isLoading)
        fun <T> success(result: T): Event<T> = Success(result)
        fun <T> failure(message: String): Event<T> = Failure(message)
    }
}