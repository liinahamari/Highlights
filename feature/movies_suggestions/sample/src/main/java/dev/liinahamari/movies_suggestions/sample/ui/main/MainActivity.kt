package dev.liinahamari.movies_suggestions.sample.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.liinahamari.movies_suggestions.sample.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
