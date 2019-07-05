package com.nooblabs.conduit.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nooblabs.conduit.R

class LoadingListView(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    var loadingBar: ProgressBar
    var rv: RecyclerView

    private var loadingListener: LoadingListener? = null

    init {
        orientation = LinearLayout.VERTICAL

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.article_list_view, this, true)

        loadingBar = getChildAt(1) as ProgressBar

        rv = getChildAt(0) as RecyclerView
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val adapter = recyclerView.adapter ?: return
                if (lastVisiblePosition == adapter.itemCount - 1) {
                    loadingListener?.onLoadMore()
                }
            }
        })

    }


    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        rv.adapter = adapter
    }

    fun setLoadingListener(listener: LoadingListener) {
        loadingListener = listener
    }

    interface LoadingListener {
        fun onLoadMore()
    }

}

@BindingAdapter("lifecycleOwner", "loading")
fun LoadingListView.bindLoader(lifecycleOwner: LifecycleOwner, loading: LiveData<Boolean>) {
    loading.observe(lifecycleOwner, Observer {
        loadingBar.visibility = if (it) View.VISIBLE else View.GONE
    })
}