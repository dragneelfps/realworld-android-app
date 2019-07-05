package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.Article
import kotlinx.coroutines.launch
import timber.log.Timber

class ArticleDetailViewModel : BaseViewModel() {

    val articleData = MutableLiveData<Article>()

    fun loadArticle(articleSlug: String) {
        loading.postValue(true)
        scope.launch {
            try {
                val article = service.getArticleBySlug(articleSlug)
                articleData.postValue(article)
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            }
        }
    }

}