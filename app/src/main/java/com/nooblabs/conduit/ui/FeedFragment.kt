package com.nooblabs.conduit.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.nooblabs.conduit.OnArticleSelectedListener
import com.nooblabs.conduit.R
import com.nooblabs.conduit.adapters.ArticleListAdapter
import com.nooblabs.conduit.databinding.FragmentFeedBinding
import com.nooblabs.conduit.databinding.SheetAddNewArticleBinding
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.toast
import com.nooblabs.conduit.viewmodels.AddNewArticleViewModel
import com.nooblabs.conduit.viewmodels.FeedViewModel
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
                    val action = FeedFragmentDirections.actionArticleDetail(article.slug)
                    findNavController().navigate(action)
                }

                override fun onAuthorClicked(username: String) {
                    val action =
                        FeedFragmentDirections.actionFeedFragment2ToUserProfileActivity2(username)
//                    val action = UserProfileActivityDirections.act(username)
                    findNavController().navigate(action)

//                    UserProfileActivityDirections.
                }
            }
        }

        binding.articleListAdapter = articleListAdapter
        binding.feedViewModel = feedViewModel

        binding.onCreateNewArticle = View.OnClickListener {

            MaterialDialog(context!!, BottomSheet()).show {

                val dialogBinding = DataBindingUtil.inflate<SheetAddNewArticleBinding>(
                    this.layoutInflater,
                    R.layout.sheet_add_new_article,
                    null,
                    false
                )
                dialogBinding.addNewArticleVM = addNewArticleViewModel.apply {
                    onCreated = {
                        Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show()
                        this@show.dismiss()
                    }
                }

                dialogBinding.lifecycleOwner = this@FeedFragment.viewLifecycleOwner
                dialogBinding.lifecycleOwner2 = this@FeedFragment.viewLifecycleOwner
                customView(view = dialogBinding.root)
            }
        }

        binding.lifecycleOwner2 = viewLifecycleOwner

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

        feedViewModel.loadUser()
        feedViewModel.loadArticles()
    }


}
