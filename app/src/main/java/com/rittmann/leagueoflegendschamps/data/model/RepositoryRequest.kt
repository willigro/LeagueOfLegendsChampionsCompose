package com.rittmann.leagueoflegendschamps.data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryProvider<T>(
    val success: ((RepositoryResource<T?>) -> Unit)? = null,
    val successVoid: ((RepositoryResource<Void?>) -> Unit)? = null,
    val error: ((RepositoryResource<T?>) -> Unit)? = null,
    val failure: ((RepositoryResource<T?>, Throwable?) -> Unit)? = null
) {
    var isVoid: Boolean = true

    init {
        isVoid = successVoid == null
    }
}

class RepositoryProviderFlow<T>(
    val success: (response: Response<T>) -> Unit,
    val error: (throwable: Throwable?) -> Unit
)

class RepositoryRequest<T> {
    lateinit var call: Call<T>

    fun makeCall(
        call: Call<T>,
        functions: RepositoryProvider<T>
    ): Call<T> {

        this.call = call
        val retrofitCall = RetrofitCall(functions)
        this.call.clone().enqueue(retrofitCall)

        return this.call
    }

    fun makeCallFlow(
        call: Call<T>,
        functions: RepositoryProviderFlow<T>
    ): Call<T> {

        this.call = call
        val retrofitCall = RetrofitCallFlow(functions)
        this.call.clone().enqueue(retrofitCall)

        return this.call
    }

    class RetrofitCall<T>(functions: RepositoryProvider<T>) : Callback<T> {

        private var mFunctions: RepositoryProvider<T> = functions

        override fun onFailure(call: Call<T>, t: Throwable) {
            mFunctions.failure?.invoke(RepositoryResource.error(t.message.orEmpty()), t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            when {
                response.raw().isSuccessful -> {
                    if (mFunctions.isVoid) {
                        mFunctions.successVoid?.invoke(
                            RepositoryResource.success(
                                null,
                                code = response.code()
                            )
                        )
                    } else {
                        mFunctions.success?.invoke(
                            RepositoryResource.success(
                                response.body(),
                                code = response.code()
                            )
                        )
                    }
                }
                else -> try {
                    mFunctions.error?.invoke(RepositoryResource.error("", code = response.code()))
                } catch (exception: Exception) {
                    mFunctions.failure?.invoke(
                        RepositoryResource.error("", code = response.code()),
                        exception
                    )
                }
            }
        }
    }

    class RetrofitCallFlow<T>(private val functions: RepositoryProviderFlow<T>) : Callback<T> {

        override fun onFailure(call: Call<T>, t: Throwable) {
            functions.error(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            functions.success(response)
        }
    }

    fun cancel() {
        if (::call.isInitialized) {
            call.cancel()
        }
    }
}

data class RepositoryResource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val code: Int? = null,
    val throwable: Throwable? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?, code: Int? = null): RepositoryResource<T> {
            return RepositoryResource(Status.SUCCESS, data, null, code)
        }

        fun <T> error(
            message: String? = null,
            data: T? = null,
            code: Int? = null,
            throwable: Throwable? = null
        ): RepositoryResource<T> {
            return RepositoryResource(Status.ERROR, data, message, code, throwable)
        }

        fun <T> loading(data: T? = null, code: Int? = null): RepositoryResource<T> {
            return RepositoryResource(Status.LOADING, data, null, code)
        }
    }
}

fun <T> genericFlowHandle(request: suspend () -> Response<T>): Flow<RepositoryResource<T>> {
    return flow {
        try {
            val response = request()
            emit(
                RepositoryResource.success(
                    response.body(),
                    code = response.code()
                )
            )
        } catch (t: Throwable) {
            emit(RepositoryResource.error<T>(throwable = t))
        }
    }
}