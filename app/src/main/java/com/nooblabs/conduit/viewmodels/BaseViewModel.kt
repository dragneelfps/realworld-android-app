package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    val scope = CoroutineScope(coroutineContext)

    fun cancelAllRequests() = coroutineContext.cancel()

    val error = MutableLiveData<Exception>()

    val loading = MutableLiveData(false)

    override fun onCleared() {
        super.onCleared()
        loading.postValue(false)
        cancelAllRequests()
    }

}