package com.nooblabs.conduit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        findNavController(R.id.user_nav_host_fragment)
            .setGraph(R.navigation.user_nav_graph, intent.extras)

        setSupportActionBar(userProfileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userProfileToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
