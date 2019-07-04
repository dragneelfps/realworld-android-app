package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.NetworkRepository
import com.nooblabs.conduit.service.PersistenceRepository
import com.nooblabs.conduit.service.Service
import com.nooblabs.conduit.service.api.ApiException
import kotlinx.coroutines.launch
import timber.log.Timber

class InitViewModel : BaseViewModel() {

    val user = MutableLiveData<User?>()
    val service = Service.get()

    fun loadUser() {
        scope.launch {
            try {
                val currentUser = service.getCurrentUser()
                user.postValue(currentUser)
            } catch (e: ApiException) {
                Timber.d("Api: $e")
                user.postValue(null)
            }
        }
    }

}