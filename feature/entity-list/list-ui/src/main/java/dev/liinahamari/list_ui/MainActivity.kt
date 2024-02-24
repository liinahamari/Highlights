package dev.liinahamari.list_ui

import android.app.Application
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.seismic.ShakeDetector
import dev.liinahamari.api.EntityListDependencies
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getCurrentFragment
import dev.liinahamari.entity_list.EntityListFactory.getApi
import dev.liinahamari.list_ui.databinding.ActivityMainBinding
import dev.liinahamari.list_ui.di.DaggerListUiComponent
import dev.liinahamari.list_ui.di.ListUiComponent
import dev.liinahamari.list_ui.tabs.SectionsPagerAdapter

class MainActivity : AppCompatActivity(R.layout.activity_main), ShakeDetector.Listener {
    private val ui by viewBinding(ActivityMainBinding::bind)
    private val adapter = SectionsPagerAdapter(this)
    internal lateinit var listUiComponent: ListUiComponent

    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        listUiComponent = DaggerListUiComponent.factory().create(getApi(object : EntityListDependencies {
            override val application: Application
                get() = getApplication()
        }))
        shakeDetector = ShakeDetector(this)
        super.onCreate(savedInstanceState)

        setupViewPager()
        setupOnBackPressed()
    }

    private fun setupViewPager() {
        ui.pager.adapter = adapter
        TabLayoutMediator(ui.tabs, ui.pager, true, false) { tab, position ->
            tab.text = Category.values()[position].emoji
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

    override fun onDestroy() {
        super.onDestroy()
        shakeDetector = null
    }

    override fun onResume() {
        super.onResume()
        shakeDetector!!.start(getSystemService(SENSOR_SERVICE) as SensorManager)
    }

    override fun hearShake() = startActivity(Intent(this, SummaryActivity::class.java))
}

interface OnBackPressedListener {
    fun onBackPressed()
}

class SummaryActivity : AppCompatActivity(R.layout.activity_summary)
