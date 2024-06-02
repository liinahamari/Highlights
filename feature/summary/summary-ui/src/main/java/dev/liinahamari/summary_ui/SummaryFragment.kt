package dev.liinahamari.summary_ui

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import dev.liinahamari.api.EntityListDependencies
import dev.liinahamari.api.domain.entities.DatabaseCounters
import dev.liinahamari.core.ext.toast
import dev.liinahamari.entity_list.EntityListFactory
import dev.liinahamari.summary.summary_ui.R
import dev.liinahamari.summary.summary_ui.databinding.FragmentSummaryBinding
import dev.liinahamari.summary_ui.di.DaggerSummaryComponent
import dev.liinahamari.summary_ui.di.SummaryComponent
import javax.inject.Inject

class SummaryFragment : Fragment(R.layout.fragment_summary) {
    private val ui by viewBinding(FragmentSummaryBinding::bind)

    private lateinit var summaryComponent: SummaryComponent

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val databaseCounterCalculationViewModel: DatabaseCounterCalculationViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        summaryComponent = DaggerSummaryComponent.builder().databaseCountersUseCase(EntityListFactory.getApi(object :
            EntityListDependencies {
            /*todo check it's singleton*/
            override val application: Application
                get() = requireActivity().application
        }).databaseCountersUseCase).build()

        summaryComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelSubscriptions()

        databaseCounterCalculationViewModel.getDatabaseCounters()
    }

    private fun renderChart(chartData: DatabaseCounters.Success) {
        AnyChart.pie().apply {
            setOnClickListener(object : ListenersInterface.OnClickListener(arrayOf("x", "value")) {
                override fun onClick(event: Event) {
                    toast(event.data["x"] + ":" + event.data["value"]) //todo tap again to rerender chart
                }
            })

            data(chartData.entities.map { ValueDataEntry(it.label, it.counter) })
            title(chartData.titleInCenterOfChart)
            labels().position("outside")

            with(legend()) {
                title()
                    .enabled(true)

                title()
                    .text("Groups")
                    .padding(0.0, 0.0, 10.0, 0.0)

                position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER)
            }
        }.also(ui.entityRatioChart::setChart)
    }

    private fun setupViewModelSubscriptions() {
        databaseCounterCalculationViewModel.errorEvent.observe(viewLifecycleOwner, ::toast)
        databaseCounterCalculationViewModel.emptyViewEvent.observe(viewLifecycleOwner) {
            ui.noDataTv.isVisible = true
            ui.entityRatioChart.isVisible = false
        }
        databaseCounterCalculationViewModel.chartDataEvent.observe(viewLifecycleOwner, ::renderChart)
    }
}
