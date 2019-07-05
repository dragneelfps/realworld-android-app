package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.ui.views.LoadingListView
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewModel : BaseViewModel() {

    val tagListData = MutableLiveData(emptyList<String>())
    val articleListByTagData = MutableLiveData(emptyList<Article>())

    var currentTag: String? = null

    fun loadTags() {
        scope.launch {
            try {
                val tags = service.getTags()
                tagListData.postValue(tags)


            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            } finally {
                loading.postValue(false)
            }
        }
    }

    fun searchArticles(tag: String? = null, more: Boolean = false) {
        currentTag = tag

        loading.postValue(true)

        scope.launch {
            try {
                if (more) {
                    val articles = service.getArticles(
                        tag = currentTag,
                        offset = articleListByTagData.value?.size
                    )
                    articleListByTagData.postValue(
                        articleListByTagData.value?.plus(articles) ?: articles
                    )
                } else {
                    val articles = service.getArticles(tag = currentTag)
                    articleListByTagData.postValue(articles)
                }
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            } finally {
                loading.postValue(false)
            }


        }

    }

    val loadingListener = object : LoadingListView.LoadingListener {
        override fun onLoadMore() {
            searchArticles(currentTag, more = true)
        }
    }


}