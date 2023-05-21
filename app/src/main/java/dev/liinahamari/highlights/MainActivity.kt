package dev.liinahamari.highlights

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.highlights.databinding.ActivityMainBinding
import dev.liinahamari.highlights.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val ui by viewBinding(ActivityMainBinding::bind)
    private val adapter = SectionsPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.viewPager.adapter = adapter
        ui.tabs.setupWithViewPager(ui.viewPager)
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
