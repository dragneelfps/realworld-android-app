package com.nooblabs.conduit.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.nooblabs.conduit.OnArticleSelectedListener
import com.nooblabs.conduit.R
import com.nooblabs.conduit.adapters.ArticleListAdapter
import com.nooblabs.conduit.databinding.FragmentSearchBinding
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.viewmodels.SearchViewModel
import timber.log.Timber


class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var articlesAdapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        searchViewModel = ViewModelProviders.of(this)[SearchViewModel::class.java]

        articlesAdapter = ArticleListAdapter().apply {
            onArticleSelectedListener = object : OnArticleSelectedListener {
                override fun onFavorite(article: Article) {

                }

                override fun onArticleSelected(article: Article) {
                    val action = SearchFragmentDirections.actionSearchToArticle(article.slug)
                    findNavController().navigate(action)
                }

                override fun onAuthorClicked(username: String) {
                    val action = SearchFragmentDirections.actionSearchToUserProfile(username)
                    findNavController().navigate(action)
                }
            }
        }

        val binding = DataBindingUtil.inflate<FragmentSearchBinding>(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        binding.searchToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.articleAdapter = articlesAdapter
        binding.searchViewModel = searchViewModel
        binding.lifeCycleOwner2 = viewLifecycleOwner

        binding.onTagClickListener = onTagClickListener

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.loadTags()
        searchViewModel.searchArticles()
        searchViewModel.articleListByTagData.observe(viewLifecycleOwner, Observer { articles ->
            articlesAdapter.setArticles(articles)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.show()
    }


    private val onTagClickListener = ChipGroup.OnCheckedChangeListener { chipGroup, i ->
        Timber.d("HERE")
        if (i != View.NO_ID) {
            val chip = chipGroup.findViewById<Chip>(i)
            searchViewModel.searchArticles(tag = chip.text.toString())
            Timber.d("HERE2")
        }
    }

}
