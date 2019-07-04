package com.nooblabs.conduit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nooblabs.conduit.databinding.ActivityHomeBinding
import com.nooblabs.conduit.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        binding.navigationListener = navigationListener

        homeViewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
    }

    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
         when(it.itemId) {
             R.id.feed_item -> {
                 Timber.d("Feed Item clicked")
                 findNavController(R.id.home_nav_host_fragment).navigate(R.id.action_global_feedFragment)
                 true
             }
             R.id.profile_item -> {
                 Timber.d("Profile Item clicked")
                 findNavController(R.id.home_nav_host_fragment).navigate(R.id.action_global_profileFragment)
                 true
             }

             else -> false
         }
    }
}
