package com.rittmann.leagueoflegendschamps.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rittmann.leagueoflegendschamps.data.model.RepositoryResource
import kotlinx.coroutines.*
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {
    protected var viewModelScopeGen: CoroutineScope? = null
    protected var _progress = MutableLiveData<Boolean>()

    protected var _errorCon by mutableStateOf(false)
        private set

    //    protected var _errorCon = SingleLiveEvent<Void>()
    protected var _errorGen = SingleLiveEvent<Void>()

    val errorCon get() = _errorCon
    val errorGen get() = _errorGen
    val isLoading get() = _progress

    protected fun showProgress() {
        _progress.postValue(true)
    }

    protected fun hideProgress() {
        _progress.postValue(false)
    }

    protected open fun handleGenericFailure() {
        _errorGen.call()
    }

    protected open fun handleConnectionFailure() {
        _errorCon = true
    }

    open fun clearViewModel() {
        _errorCon = false
        _errorGen = SingleLiveEvent()
    }

    fun executeAsyncProgress(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        scope: CoroutineScope = GlobalScope,
        block: suspend () -> Unit
    ) {
        val s = viewModelScopeGen ?: scope
        s.launch {
            withContext(dispatcher) {
                _progress.postValue(true)
                block()
                _progress.postValue(false)
            }
        }
    }

    fun executeAsync(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        blockOnMain: Boolean = false,
        scope: CoroutineScope = GlobalScope,
        block: suspend () -> Unit
    ) {
        val s = viewModelScopeGen ?: scope
        s.launch {
            if (blockOnMain)
                withContext(Dispatchers.Main) {
                    block()
                }
            else
                block()
        }
    }

    fun executeMain(
        scope: CoroutineScope = GlobalScope,
        block: suspend () -> Unit
    ) {
        val s = viewModelScopeGen ?: scope
        s.launch {
            withContext(Dispatchers.Main) {
                block()
            }
        }
    }

    fun <T> tryHandleResponse(response: RepositoryResource<T>): Boolean {
        when (response.status) {
            RepositoryResource.Status.ERROR -> {
                executeMain { showProgress() }

                response.throwable?.also { t ->
                    if (t is UnknownHostException) {
                        executeMain { handleConnectionFailure() }

                        return true
                    }
                }
            }
            RepositoryResource.Status.LOADING -> executeMain { showProgress() }
            else -> {
                clearViewModel()
            }
        }

        return false
    }
}