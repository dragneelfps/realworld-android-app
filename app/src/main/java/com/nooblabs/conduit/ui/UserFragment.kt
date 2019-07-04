package com.nooblabs.conduit.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nooblabs.conduit.OnArticleSelectedListener

import com.nooblabs.conduit.R
import com.nooblabs.conduit.adapters.ArticleListAdapter
import com.nooblabs.conduit.databinding.FragmentUserBinding
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : Fragment() {

    private val args by navArgs<UserFragmentArgs>()

    private lateinit var userViewModel: UserViewModel

    private lateinit var userArticleAdapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProviders.of(this)[UserViewModel::class.java]

        userArticleAdapter = ArticleListAdapter().apply {
            onArticleSelectedListener = object: OnArticleSelectedListener {
                override fun onFavorite(article: Article) {
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

        val binding = DataBindingUtil.inflate<FragmentUserBinding>(
            inflater,
            R.layout.fragment_user,
            container,
            false
        )

        binding.userViewModel = userViewModel
        binding.articleAdapter = userArticleAdapter

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userArticlesData.observe(viewLifecycleOwner, Observer { userArticles ->
            userArticleAdapter.setArticles(userArticles)
        })

        userViewModel.loadUserData(args.username)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(userToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        userToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


}
