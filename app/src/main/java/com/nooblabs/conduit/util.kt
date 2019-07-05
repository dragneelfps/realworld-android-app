package com.nooblabs.conduit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.*
import androidx.core.content.getSystemService
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.navigation.NavigationView
import com.nooblabs.conduit.models.Article
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber

fun Context?.toast(message: CharSequence) = if (this != null)
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show() else Unit

interface OnArticleSelectedListener {
    fun onFavorite(article: Article)
    fun onArticleSelected(article: Article)
    fun onAuthorClicked(username: String)
}

interface OnArticleDetailListener {
    fun onAuthorClicked(username: String)
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService<ConnectivityManager>()
    connectivityManager ?: return false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkInfo = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(networkInfo)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN))
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected && (networkInfo.type in arrayOf(
            ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_MOBILE,
            ConnectivityManager.TYPE_VPN
        ))
    }
}

@BindingAdapter("profileImage")
fun ImageView.setProfileImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_person_black_24dp)
        .error(R.drawable.ic_person_black_24dp)
        .apply(RequestOptions().circleCrop())
        .into(this)
}

@BindingAdapter("date")
fun TextView.setDate(dateStr: String?) {
    if(dateStr == null) {
        text = null
        return
    }
    val date = DateTime.parse(dateStr)
    val fmt =
        if (date.year == DateTime.now().year)
            DateTimeFormat.forPattern("HH:mm, dd MMM")
        else
            DateTimeFormat.forPattern("HH:mm, dd MMM YY")
    text = fmt.print(date)
}

@BindingAdapter(value = ["chipsData", "lifecycleOwner"], requireAll = false)
fun ChipGroup.bindChips(chipsData: LiveData<List<String>>, lifecycleOwner: LifecycleOwner?) {

    if(lifecycleOwner == null) {
        return
    }

    chipsData.observe(lifecycleOwner, Observer {  newData ->
        removeAllViews()
        newData.map {
            Chip(context).apply {
                text = it
                isCheckable = true
            }
        }.forEach { addView(it) }
    })
}

@BindingAdapter("itemResource", "data")
fun Spinner.initSpinner(resource: Int, data: Int) {
    ArrayAdapter.createFromResource(
        context,
        data,
        resource
    ).also { adapter ->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
}

@BindingAdapter("itemSelectionListener")
fun Spinner.setItemSelectionListener(listener: AdapterView.OnItemSelectedListener) {
    onItemSelectedListener = listener
}

@BindingAdapter("navItemSelectedListener")
fun NavigationView.setNavItemSelectedListener(listener: NavigationView.OnNavigationItemSelectedListener) {
    Timber.d("HERE434")
    setNavigationItemSelectedListener(listener)
}

@BindingAdapter("scrollListener")
fun RecyclerView.setScrollListener(listener: RecyclerView.OnScrollListener) {
    addOnScrollListener(listener)
}

fun List<Article>?.updateArticle(newArticle: Article): List<Article> {
    this ?: return listOf(newArticle)

    val i = this.indexOfFirst { it.slug == newArticle.slug }

    val newArticles = this.subList(0, i) + newArticle + this.subList(i + 1, this.size)

    return newArticles
}

enum class ListType {
    ALL, FEED
}

enum class LoadType {
    RELOAD, MORE
}


interface OnUserProfileListener {
    fun toggleFollow()
    fun onEditProfile()
}
