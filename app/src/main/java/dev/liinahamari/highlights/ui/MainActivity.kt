package dev.liinahamari.highlights.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.ActivityMainBinding
import dev.liinahamari.highlights.di.components.DaggerMainComponent
import dev.liinahamari.highlights.di.components.MainComponent
import dev.liinahamari.highlights.helper.getCurrentFragment
import dev.liinahamari.highlights.ui.tabs.SectionsPagerAdapter
import dev.liinahamari.highlights.ui.tabs.TAB_TITLES

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var appComponent: MainComponent

    private val ui by viewBinding(ActivityMainBinding::bind)
    private val adapter = SectionsPagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = DaggerMainComponent.builder().application(application).build()
        super.onCreate(savedInstanceState)
        setupViewPager()
        setupOnBackPressed()
    }

    private fun setupViewPager() {
        ui.pager.adapter = adapter
        TabLayoutMediator(ui.tabs, ui.pager, true, false) { tab, position ->
            tab.text = TAB_TITLES[position].toString().replace('_', '-')
        }.attach()
    }

    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                with(ui.pager.getCurrentFragment(supportFragmentManager)) {
                    if (this !is OnBackPressedListener) return
                    this.onBackPressed()
                }
            }
        })
    }
}

interface OnBackPressedListener {
    fun onBackPressed()
}
