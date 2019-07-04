package com.nooblabs.conduit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nooblabs.conduit.OnArticleSelectedListener
import com.nooblabs.conduit.R
import com.nooblabs.conduit.databinding.ArticleItemBinding
import com.nooblabs.conduit.models.Article

class ArticleListAdapter : RecyclerView.Adapter<ArticleListAdapter.ArticleListViewHolder>() {

    private val articlesList = ArrayList<Article>()

    var onArticleSelectedListener: OnArticleSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder {
        val binding = DataBindingUtil.inflate<ArticleItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.article_item,
            parent,
            false
        )
        return ArticleListViewHolder(binding)
    }

    override fun getItemCount() = articlesList.size

    override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
        holder.bind(articlesList[position], onArticleSelectedListener)
    }

    fun setArticles(articles: List<Article>) {
        articlesList.clear()
        articlesList.addAll(articles)
        notifyDataSetChanged()
    }


    class ArticleListViewHolder(val articleItemBinding: ArticleItemBinding) :
        RecyclerView.ViewHolder(articleItemBinding.root) {
        fun bind(article: Article, onArticleSelectedListener: OnArticleSelectedListener?){
            articleItemBinding.article = article
            articleItemBinding.onArticleSelectedListener = onArticleSelectedListener
            itemView.setOnClickListener { onArticleSelectedListener?.onArticleSelected(article) }
            articleItemBinding.executePendingBindings()
        }
    }
}