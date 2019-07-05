package com.nooblabs.conduit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.nooblabs.conduit.databinding.ActivityHomeBinding
import com.nooblabs.conduit.databinding.LayoutNavHeaderBinding
import com.nooblabs.conduit.viewmodels.HomeViewModel
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]


        val binding =
            DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        setSupportActionBar(binding.homeToolbar)

        val actionBarToggle = ActionBarDrawerToggle(
            this,
            binding.homeDrawer,
            binding.homeToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.homeDrawer.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

//        binding.navigationListener = navigationListener
        binding.drawerNavItemListener = drawerNavItemListener
        binding.homeViewModel = homeViewModel

        val navBinding = DataBindingUtil.inflate<LayoutNavHeaderBinding>(
            layoutInflater,
            R.layout.layout_nav_header,
            binding.navView,
            false
        )
        binding.navView.addHeaderView(navBinding.root)
        navBinding.user = homeViewModel.currentUserData
        navBinding.lifecycleOwner = this

        binding.lifecycleOwner = this
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_search -> {
                findNavController(R.id.home_nav_host_fragment).navigate(R.id.action_global_searchFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val drawerNavItemListener = NavigationView.OnNavigationItemSelectedListener {
        Timber.d("HERE2434")
        when (it.itemId) {
            R.id.item_nav_profile -> {
                Timber.d("HERE213241")
                findNavController(R.id.home_nav_host_fragment).navigate(
                    R.id.userProfileActivity,
                    Bundle().apply {
                        putString(
                            "username",
                            homeViewModel.currentUserData.value!!.username
                        )
                    })
                true
            }
            else -> false
        }
    }

//    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
//        when (it.itemId) {
//            R.id.feed_item -> {
//                Timber.d("Feed Item clicked")
//                findNavController(R.id.home_nav_host_fragment).navigate(R.id.action_global_feedFragment)
//                true
//            }
//            R.id.search_item -> {
//                findNavController(R.id.home_nav_host_fragment).navigate(R.id.action_global_searchFragment)
//                true
//            }
//            R.id.profile_item -> {
//                Timber.d("Profile Item clicked")
//                findNavController(R.id.home_nav_host_fragment).navigate(R.id.action_global_profileFragment)
//                true
//            }
//
//            else -> false
//        }
//    }
}
