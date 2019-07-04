package com.nooblabs.conduit.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.nooblabs.conduit.ListType
import com.nooblabs.conduit.OnArticleSelectedListener

import com.nooblabs.conduit.R
import com.nooblabs.conduit.adapters.ArticleListAdapter
import com.nooblabs.conduit.databinding.FragmentFeedBinding
import com.nooblabs.conduit.databinding.SheetAddNewArticleBinding
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.toast
import com.nooblabs.conduit.viewmodels.AddNewArticleViewModel
import com.nooblabs.conduit.viewmodels.FeedViewModel
import com.nooblabs.conduit.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_feed.*
import timber.log.Timber

class FeedFragment : Fragment() {
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var addNewArticleViewModel: AddNewArticleViewModel
    private lateinit var articleListAdapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel = ViewModelProviders.of(this)[FeedViewModel::class.java]
        addNewArticleViewModel = ViewModelProviders.of(this)[AddNewArticleViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentFeedBinding>(
            inflater,
            R.layout.fragment_feed,
            container,
            false
        )

        articleListAdapter = ArticleListAdapter().apply {
            onArticleSelectedListener = object: OnArticleSelectedListener {
                override fun onFavorite(article: Article) {
                    feedViewModel.toggleFavorite(article)
                }

                override fun onArticleSelected(article: Article) {
                    val action = ArticleDetailFragmentDirections.actionGlobalArticleDetailFragment(article.slug)
                    findNavController().navigate(action)
                }

                override fun onAuthorClicked(username: String) {
                    val action = UserFragmentDirections.actionGlobalUserDetails(username)
                    findNavController().navigate(action)
                }
            }
        }

        binding.articleListAdapter = articleListAdapter
        binding.feedViewModel = feedViewModel

        binding.articleListTypeListener = articleListTypeListener

        binding.onCreateNewArticle = View.OnClickListener {

            MaterialDialog(context!!, BottomSheet()).show {

                val dialogBinding = DataBindingUtil.inflate<SheetAddNewArticleBinding>(
                    this.layoutInflater,
                    R.layout.sheet_add_new_article,
                    null,
                    false
                )
                dialogBinding.addNewArticleVM = addNewArticleViewModel

                dialogBinding.lifecycleOwner = this@FeedFragment.viewLifecycleOwner
                dialogBinding.lifecycleOwner2 = this@FeedFragment.viewLifecycleOwner
                customView(view = dialogBinding.root)
            }
        }

        binding.scrollListener = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if(lastVisiblePosition == recyclerView.childCount) {
                    when(feedViewModel.listType.value) {
                        ListType.ALL -> feedViewModel.loadArticles(more = true)
                        ListType.FEED -> feedViewModel.loadArticles(more = true, feed = true)
                    }
                }
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel.articlesData.observe(viewLifecycleOwner, Observer { articles ->
            Timber.d("articlesData: $articles")
            articleListAdapter.setArticles(articles)
        })

        feedViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            Timber.d("error: $error")
            context.toast("ERROR: $error")
        })

        feedViewModel.loadArticles()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(homeToolbar)
    }

    private val articleListTypeListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) = Unit

        override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
            when(parent.getItemAtPosition(pos).toString()) {
                "All" -> feedViewModel.loadArticles()
                "Feed" -> feedViewModel.loadArticles(feed = true)
            }
        }

    }


}
