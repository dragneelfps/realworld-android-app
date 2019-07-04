package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.Service
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel : BaseViewModel() {


    val currentUser = MutableLiveData<User?>()

    val editMode = MutableLiveData(false)

    val service = Service.get()

    val usernameData = MutableLiveData<String>()
    val emailData = MutableLiveData<String>()
    val bioData = MutableLiveData<String>()

    fun loadUser() {
        loading.postValue(true)
        scope.launch {
            val storedUser = service.getCurrentUser()
            currentUser.postValue(storedUser)
            if (storedUser != null) {
                updateView(storedUser)
            }
            loading.postValue(false)
        }
    }

    fun onSave() {
        editMode.postValue(false)
        val username = usernameData.value
        val email = emailData.value
        val bio = bioData.value
        updateUser(
            username = username,
            email = email,
            bio = bio
        )
    }

    fun toggleEditingMode(mode: Boolean) {
        editMode.postValue(mode)
    }

    fun changeImage(imageUrl: String) {
        updateUser(image = imageUrl)
    }

    fun logoutUser() {
        scope.launch {
            service.logoutUser()
            currentUser.postValue(null)
        }
    }

    private fun updateUser(
        username: String? = null,
        email: String? = null,
        password: String? = null,
        bio: String? = null,
        image: String? = null
    ) {
        scope.launch {
            Timber.d("Saving: $username, $email, $password, $bio, $image")
            try {
                val user = service.updateUser(
                    username, email, password, bio, image
                )
                currentUser.postValue(user)
                if (user != null) {
                    updateView(user)
                }
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            }
        }
    }

    private fun updateView(user: User) {
        usernameData.postValue(user.username)
        emailData.postValue(user.email)
        bioData.postValue(user.bio)
    }

    fun getImageUrl() = currentUser.value?.image

    fun changePassword(newPassword: String) {
        updateUser(password = newPassword)
    }


}