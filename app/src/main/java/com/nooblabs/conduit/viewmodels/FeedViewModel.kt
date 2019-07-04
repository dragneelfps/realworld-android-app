package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.ListType
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.Service
import com.nooblabs.conduit.service.api.UnauthorizedException
import com.nooblabs.conduit.updateArticle
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedViewModel : BaseViewModel() {

    val articlesData = MutableLiveData(emptyList<Article>())

    val service = Service.get()

    val currentUserData = MutableLiveData<User>()

    val listType = MutableLiveData(ListType.ALL)

    fun loadArticles(more: Boolean = false, feed: Boolean = false) {
        clearArticleList()
        loading.postValue(true)
        if(feed)
            listType.postValue(ListType.FEED)
        else
            listType.postValue(ListType.ALL)
        scope.launch {
            try{
                currentUserData.postValue(service.getCurrentUser())
                val articles = when {
                    more && feed -> service.getFeed(offset = getOffset())
                    more && !feed -> service.getArticles(offset = getOffset())
                    !more && feed -> service.getFeed()
                    !more && !feed -> service.getArticles()
                    else -> emptyList()
                }
                if(more) {
                    articlesData.postValue(articlesData.value!!.plus(articles))
                } else {
                    articlesData.postValue(articles)
                }
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            } finally {
                loading.postValue(false)
            }
        }
    }

    fun toggleFavorite(article: Article) {
        val favorite = !article.favorited
        scope.launch {
            try {
                val currentUser = service.getCurrentUser()
                currentUser ?: return@launch run { error.postValue(UnauthorizedException()) }
                val newArticle = service.toggleFavoriteArticle(article.slug, favorite) ?: return@launch
                val newArticles = articlesData.value.updateArticle(newArticle)
                articlesData.postValue(newArticles)
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            }
        }
    }

    private fun clearArticleList() {
        articlesData.postValue(emptyList())
    }

    private fun getOffset() = articlesData.value?.size ?: 0


}