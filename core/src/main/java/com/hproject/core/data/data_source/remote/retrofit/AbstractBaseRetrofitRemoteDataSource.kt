package com.hproject.core.data.data_source.remote.retrofit

import com.hproject.core.domain.authorization.UnAuthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

/**
 * Retrofit 기반 RemoteDataSource Base Class
 *
 * @author hjkim
 * @since 2023/02/15
 **/
abstract class AbstractBaseRetrofitRemoteDataSource {
    companion object {
        const val CODE_UNAUTHORIZED: Int = 401
    }

    /**
     * Remote Api Call 결과 처리
     *
     * @param callAction  수행할 api call
     * @author hjkim
     * @since 2023/02/15
     **/
    suspend inline fun <T> runWithHandlingResult(crossinline callAction: suspend () -> Response<T>): Result<T?> {
        return try {
            withContext(Dispatchers.IO) {
                val response = callAction()

                withContext(Dispatchers.Main) {
                    when {
                        // statusCode 200..300
                        response.isSuccessful -> {
                            Result.success(response.body())
                        }
                        // unauthorized 401
                        response.code() == CODE_UNAUTHORIZED -> {
                            Result.failure(UnAuthorizedException())
                        }
                        else -> {
                            Result.failure(exception = Exception(Throwable(message = "UnknownException")))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Timber.e(e)
                Result.failure(e)
            }
        }
    }
}