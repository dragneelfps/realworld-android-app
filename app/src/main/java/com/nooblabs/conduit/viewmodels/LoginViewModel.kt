package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.User
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel : BaseViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    val userLogin = MutableLiveData<User>()

    lateinit var goToRegister: () -> Unit


    fun onSubmit() {

        loading.postValue(true)

        if (!validate()) return

        scope.launch {
            try {
                val user = service.loginUser(email.value!!, password.value!!)
                userLogin.postValue(user)
            } catch (e: Exception) {
                Timber.d("Error: $e")
                error.postValue(e)
            } finally {
                loading.postValue(false)
            }
        }
    }

    private fun validate(): Boolean {
        if (email.value.isNullOrEmpty()) {
            emailError.postValue("emailData can't be empty")
            return false
        }
        if (password.value.isNullOrEmpty()) {
            passwordError.postValue("password can't be empty")
            return false
        }
        return true
    }

    fun onInputChanged(s: CharSequence, start: Int, end: Int, count: Int) {
        emailError.postValue(null)
        passwordError.postValue(null)
    }


}