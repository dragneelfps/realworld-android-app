package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.models.Profile
import kotlinx.coroutines.launch
import timber.log.Timber

class UserViewModel : BaseViewModel() {

    val profileData = MutableLiveData<Profile>()

    val userArticlesData = MutableLiveData<List<Article>>()

    val isMe = MutableLiveData(false)

    fun loadUserData(username: String) {
        loading.postValue(true)
        scope.launch {
            try {

                val currentUser = service.getCurrentUser()
                if (currentUser != null && currentUser.username == username) {
                    isMe.postValue(true)
                } else {
                    isMe.postValue(false)
                }
                val profile = service.getProfile(username)
                profileData.postValue(profile)
                val userArticles = service.getArticles(author = profile.username)
                userArticlesData.postValue(userArticles)
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            }
        }
    }

    fun toggleFollow() {
        scope.launch {
            try {
                val profile = profileData.value ?: return@launch
                val follow = !profile.following
                val updatedProfile = service.toggleFollow(profile.username, follow)
                profileData.postValue(updatedProfile)
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            }
        }
    }

}