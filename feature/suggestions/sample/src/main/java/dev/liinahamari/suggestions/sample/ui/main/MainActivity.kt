package dev.liinahamari.suggestions.sample.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.suggestions.sample.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(Category.GOOD))
                .commitNow()
        }
    }
}
