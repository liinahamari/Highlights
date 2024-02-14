package dev.liinahamari.summary_ui

import android.app.Application
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.Typeface.MONOSPACE
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.github.mikephil.charting.utils.ColorTemplate.JOYFUL_COLORS
import com.github.mikephil.charting.utils.ColorTemplate.LIBERTY_COLORS
import com.github.mikephil.charting.utils.ColorTemplate.PASTEL_COLORS
import com.github.mikephil.charting.utils.ColorTemplate.VORDIPLOM_COLORS
import com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue
import com.github.mikephil.charting.utils.MPPointF
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

        setupTotalStatisticsChart()
        databaseCounterCalculationViewModel.getDatabaseCounters()
    }

    private fun setupTotalStatisticsChart() { // todo filter empty
        ui.entityRatioChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            setCenterTextTypeface(MONOSPACE)
            setCenterTextColor(WHITE)
            isDrawHoleEnabled = true
            setHoleColor(BLACK)
            setTransparentCircleColor(WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutQuad)
            setEntryLabelColor(BLACK)
            setEntryLabelTypeface(MONOSPACE)
            setEntryLabelTextSize(12f)
        }
    }

    private fun setChartData(chartData: DatabaseCounters) = when (chartData) {
        is DatabaseCounters.Empty -> {
            ui.noDataTv.isVisible = true
            ui.entityRatioChart.isVisible = false
        }

        is DatabaseCounters.DatabaseCorruptionError -> toast("Something wrong with database!")
        is DatabaseCounters.Success -> renderChart(chartData)
    }

    private fun renderChart(chartData: DatabaseCounters.Success) {
        val entries = chartData.entities.map { PieEntry(it.counter, it.label) }
        ui.entityRatioChart.centerText = chartData.titleInCenterOfChart

        ui.entityRatioChart.data = PieDataSet(entries, "Total").apply {
            setDrawIcons(false)
            sliceSpace = 3f
            iconsOffset = MPPointF(0f, 40f)
            selectionShift = 5f
            colors = VORDIPLOM_COLORS.toList() + JOYFUL_COLORS.toList() + COLORFUL_COLORS.toList() +
                    LIBERTY_COLORS.toList() + PASTEL_COLORS.toList() + getHoloBlue()
        }.let(::PieData).apply {
            setValueFormatter(object : PercentFormatter() {
                override fun getPieLabel(value: Float, pieEntry: PieEntry?): String =
                    "${String.format("%.2f", value)}% (${pieEntry?.value?.toInt()})"
            })
            setValueTextSize(11f)
            setValueTextColor(BLACK)
            setValueTypeface(MONOSPACE)
        }

        ui.entityRatioChart.highlightValues(null)
        ui.entityRatioChart.invalidate()
    }

    private fun setupViewModelSubscriptions() =
        databaseCounterCalculationViewModel.combinedChartData.observe(viewLifecycleOwner, ::setChartData)
}
