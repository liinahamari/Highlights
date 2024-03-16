package dev.liinahamari.list_ui.activities

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.seismic.ShakeDetector
import dev.liinahamari.api.EntityListDependencies
import dev.liinahamari.core.ext.getCurrentFragment
import dev.liinahamari.core.ext.sensorManager
import dev.liinahamari.entity_list.EntityListFactory.getApi
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.ActivityMainBinding
import dev.liinahamari.list_ui.di.DaggerListUiComponent
import dev.liinahamari.list_ui.di.ListUiComponent
import dev.liinahamari.list_ui.tabs.SectionsPagerAdapter
import dev.liinahamari.list_ui.tabs.ViewPagerPlaceholderFragment
import dev.liinahamari.list_ui.viewmodels.MainActivityViewModel
import javax.inject.Inject

internal const val RETURN_CODE_SUCCESS = 100

class MainActivity : AppCompatActivity(R.layout.activity_main), ShakeDetector.Listener {
    private val ui by viewBinding(ActivityMainBinding::bind)
    private val adapter = SectionsPagerAdapter(this)
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    internal lateinit var listUiComponent: ListUiComponent

    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        listUiComponent = DaggerListUiComponent.factory().create(getApi(object : EntityListDependencies {
            override val application: Application
                get() = getApplication()
        }))
        listUiComponent.inject(this)
        shakeDetector = ShakeDetector(this)
        super.onCreate(savedInstanceState)

        setupViewPager()
        setupOnBackPressed()
    }

    private fun setupViewPager() {
        ui.pager.adapter = adapter
        TabLayoutMediator(ui.tabs, ui.pager, true, false) { tab, position ->
            tab.text = ViewPagerPlaceholderFragment.ViewPagerEntries.values()[position].emoji
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
        shakeDetector!!.start(sensorManager)
    }

    override fun hearShake() {
        if (viewModel.getShaked().not()) {
            viewModel.setShaked(true)
            summaryActivityWithResult.launch(Intent(this, SummaryActivity::class.java))
        }
    }

    private val summaryActivityWithResult = registerForActivityResult(StartActivityForResult()) { res: ActivityResult ->
        if (res.resultCode == RETURN_CODE_SUCCESS) {
            viewModel.setShaked(false)
        }
    }

    interface OnBackPressedListener {
        fun onBackPressed()
    }
}
