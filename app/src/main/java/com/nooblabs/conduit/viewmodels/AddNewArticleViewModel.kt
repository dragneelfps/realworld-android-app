package com.nooblabs.conduit.viewmodels

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AddNewArticleViewModel : BaseViewModel() {

    val titleData = MutableLiveData("")
    val descriptionData = MutableLiveData("")
    val bodyData = MutableLiveData("")
    val tagListData = MutableLiveData(emptyList<String>())
    val newTagData = MutableLiveData("")

    lateinit var onCreated: () -> Unit

    fun createArticle() {
        if (!validate()) {
            error.postValue(Exception("Fill all details"))
            return
        }
        val title = titleData.value!!
        val description = descriptionData.value!!
        val body = bodyData.value!!
        val tagList = tagListData.value!!

        scope.launch {
            try {
                val article = service.createArticle(title, description, body, tagList)
                launch(Dispatchers.Main) {
                    onCreated()
                }

            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            }
        }

    }

    private fun validate(): Boolean {
        return arrayListOf(titleData, descriptionData, bodyData).map { it.value.isNullOrEmpty() }
            .any()
    }

    fun onAddChip() {
        val data = newTagData.value
        newTagData.postValue("")
        if (data.isNullOrEmpty()) return
        val newTagList = tagListData.value!!.filter { it != data }.plus(data)
        tagListData.postValue(newTagList)
    }

    fun clearData() {
        titleData.postValue("")
        descriptionData.postValue("")
        bodyData.postValue("")
        tagListData.postValue(emptyList())
        newTagData.postValue("")
    }

}