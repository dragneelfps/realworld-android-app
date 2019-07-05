package com.nooblabs.conduit.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nooblabs.conduit.OnArticleSelectedListener
import com.nooblabs.conduit.OnUserProfileListener
import com.nooblabs.conduit.R
import com.nooblabs.conduit.adapters.ArticleListAdapter
import com.nooblabs.conduit.databinding.FragmentUserBinding
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.viewmodels.UserViewModel


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
                    val action = UserFragmentDirections.actionUserArticleDetail(article.slug)
                    findNavController().navigate(action)
                }

                override fun onAuthorClicked(username: String) {
                    val action = UserFragmentDirections.actionUserToUser(username)
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

        binding.userProfileListener = userProfileListener

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

    private val userProfileListener = object : OnUserProfileListener {
        override fun toggleFollow() {
            userViewModel.toggleFollow()
        }

        override fun onEditProfile() {
            findNavController().navigate(R.id.profileActivity)
        }
    }


}
