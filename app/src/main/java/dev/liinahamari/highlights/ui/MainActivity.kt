package dev.liinahamari.highlights.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.ActivityMainBinding
import dev.liinahamari.highlights.ui.main.SectionsPagerAdapter
import dev.liinahamari.highlights.ui.main.TAB_TITLES

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val ui by viewBinding(ActivityMainBinding::bind)
    private val adapter = SectionsPagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.pager.adapter = adapter
        TabLayoutMediator(ui.tabs, ui.pager) { tab, position ->
            tab.text = TAB_TITLES[position].toString().replace('_', ' ')
        }.attach()

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                with(supportFragmentManager.fragments.first()) {
                    if (this !is IOnBackPressed) return
                    this.onBackPressed()
                }
            }
        })
    }
}

interface IOnBackPressed {
    fun onBackPressed()
}
