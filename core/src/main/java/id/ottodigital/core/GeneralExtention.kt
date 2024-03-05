package id.ottodigital.core

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.getErrorMessage() =
        when (this) {
            is HttpException -> ""
            is SocketTimeoutException -> ""
            is IOException -> ""
            else -> ""
        }

fun Throwable.getErrorPaymentMessage() =
        when (this) {
            is HttpException -> ""
            is SocketTimeoutException -> ""
            is IOException -> ""
            else -> ""
        }