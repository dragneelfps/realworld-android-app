package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.Service
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    val scope = CoroutineScope(coroutineContext)

    fun cancelAllRequests() = coroutineContext.cancel()

    val error = MutableLiveData<Exception>()

    val loading = MutableLiveData(false)

    val service = Service.get()

    val currentUserData = MutableLiveData<User>()

    override fun onCleared() {
        super.onCleared()
        loading.postValue(false)
        cancelAllRequests()
    }

    open fun loadUser() {
        scope.launch {
            try {
                currentUserData.postValue(service.getCurrentUser())
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            }
        }
    }

}