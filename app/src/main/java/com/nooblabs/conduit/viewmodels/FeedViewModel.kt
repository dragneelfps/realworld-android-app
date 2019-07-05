package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.service.api.UnauthorizedException
import com.nooblabs.conduit.ui.views.LoadingListView
import com.nooblabs.conduit.updateArticle
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedViewModel : BaseViewModel() {

    val articlesData = MutableLiveData(emptyList<Article>())

    fun loadArticles(more: Boolean = false) {
//        if(!more) clearArticleList()
        loading.postValue(true)
        scope.launch {
            try{
                if(more) {
                    val articles = service.getFeed(offset = getOffset())
                    articlesData.postValue(articlesData.value?.plus(articles) ?: articles)
                } else {
                    val articles = service.getFeed()
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

    private fun getOffset() = articlesData.value?.size ?: 0

    val loadingListener = object : LoadingListView.LoadingListener {
        override fun onLoadMore() {
            loadArticles(more = true)
        }
    }


}