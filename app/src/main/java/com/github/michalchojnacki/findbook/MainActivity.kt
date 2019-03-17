package com.github.michalchojnacki.findbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.github.michalchojnacki.findbook.ui.booklist.BookListFragment
import com.github.michalchojnacki.findbook.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_QUERY = "com.github.michalchojnacki.findbook.MainActivity.EXTRA_QUERY"

        @JvmOverloads
        fun getCallingIntent(context: Context, query: String? = null) =
            Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_QUERY, query)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.transaction {
                val extraQuery = intent?.extras?.getString(EXTRA_QUERY)
                if (extraQuery?.isNotBlank() == true) {
                    replace(R.id.container, BookListFragment.newInstance(extraQuery))
                } else {
                    replace(R.id.container, MainFragment.newInstance())
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
