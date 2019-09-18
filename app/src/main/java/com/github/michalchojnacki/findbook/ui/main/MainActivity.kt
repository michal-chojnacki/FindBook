package com.github.michalchojnacki.findbook.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.ui.booklist.BookListFragment

class MainActivity : AppCompatActivity() {

    private val queryArg: String?
        get() = intent?.extras?.getString(EXTRA_QUERY)

    companion object {
        private const val EXTRA_QUERY =
            "com.github.michalchojnacki.findbook.ui.main.MainActivity.EXTRA_QUERY"

        @JvmOverloads
        @JvmStatic
        fun getCallingIntent(context: Context, query: String? = null) =
            Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_QUERY, query)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportFragmentManager.commit {
            val nextFragment = queryArg?.takeIf { it.isNotBlank() }?.let { query ->
                BookListFragment.newInstance(query)
            } ?: MainFragment.newInstance()
            replace(R.id.container, nextFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
