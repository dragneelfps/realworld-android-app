package com.nooblabs.conduit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_article.*

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        findNavController(R.id.article_nav_host_fragment)
            .setGraph(R.navigation.article_nav_graph, intent.extras)

        setSupportActionBar(articleToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        articleToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
