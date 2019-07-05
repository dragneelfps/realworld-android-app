package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.User
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterViewModel : BaseViewModel() {

    val username = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    val usernameError = MutableLiveData<String>()
    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    lateinit var goToLogin: () -> Unit

    var userRegistration = MutableLiveData<User>()


    fun onSubmit() {

        loading.postValue(true)

        if (!validate()) return

        scope.launch {
            try {
                val user = service.registerUser(username.value!!, email.value!!, password.value!!)
                userRegistration.postValue(user)
            } catch (e: Exception) {
                Timber.d("Error: $e")
                error.postValue(e)
            } finally {
                loading.postValue(false)
            }
        }
    }

    private fun validate(): Boolean {
        if (username.value.isNullOrEmpty()) {
            usernameError.postValue("usernameData can't be empty")
            return false
        }
        if (email.value.isNullOrEmpty()) {
            emailError.postValue("emailData can't be empty")
            return false
        }
        if (!email.value!!.contains("@")) {
            emailError.postValue("invalid emailData")
            return false
        }
        if (password.value.isNullOrEmpty()) {
            passwordError.postValue("password can't be empty")
            return false
        }
        if (password.value!!.length < 8) {
            passwordError.postValue("password should be of at least 8 characters")
            return false
        }
        return true
    }


    fun onInputChanged(s: CharSequence, start: Int, end: Int, count: Int) {
        emailError.postValue(null)
        usernameError.postValue(null)
        passwordError.postValue(null)
    }


}