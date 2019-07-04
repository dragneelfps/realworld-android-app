package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.Service
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    val service = Service.get()

    val currentUser = MutableLiveData<User?>()

    fun loadUser() {
        scope.launch {
            currentUser.postValue(service.getCurrentUser())
        }
    }

}