package com.nooblabs.conduit.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.nooblabs.conduit.OnArticleDetailListener
import com.nooblabs.conduit.R
import com.nooblabs.conduit.databinding.FragmentArticleDetailBinding
import com.nooblabs.conduit.viewmodels.ArticleDetailViewModel


class ArticleDetailFragment : Fragment() {

    val args by navArgs<ArticleDetailFragmentArgs>()

    lateinit var articleDetailViewModel: ArticleDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        articleDetailViewModel = ViewModelProviders.of(this)[ArticleDetailViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentArticleDetailBinding>(
            inflater,
            R.layout.fragment_article_detail,
            container,
            false
        )
        binding.articleDetailViewModel = articleDetailViewModel

        binding.articleDetailListener = object : OnArticleDetailListener {
            override fun onAuthorClicked(username: String) {
//                val action = UserFragmentDirections.actionGlobalUserProfile(username)
//                findNavController().navigate(action)
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleDetailViewModel.loadArticle(args.articleSlug)
    }


}
