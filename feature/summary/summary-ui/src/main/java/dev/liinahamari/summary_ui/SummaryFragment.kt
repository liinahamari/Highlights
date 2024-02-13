package dev.liinahamari.summary_ui

import android.app.Application
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.Typeface.MONOSPACE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
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
            EntityListDependencies {/*todo check it's singleton*/
            override val application: Application
                get() = requireActivity().application
        }).databaseCountersUseCase).build()

        summaryComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelSubscriptions()

        with(databaseCounterCalculationViewModel) {
            getTotalAmount()
            getMoviesAmount()
            getDocumentariesAmount()
            getBooksAmount()
            getGamesAmount()
        }
        renderEntriesTotalStatisticsChart(100f, 30f, 10f, 0f)
    }

    private fun renderEntriesTotalStatisticsChart(
        totalMovies: Float,
        totalDocumentaries: Float,
        totalBooks: Float,
        totalGames: Float
    ) {
        ui.entityRatioChart.setUsePercentValues(true)
        ui.entityRatioChart.description.isEnabled = false
        ui.entityRatioChart.setExtraOffsets(5f, 10f, 5f, 5f)
        ui.entityRatioChart.dragDecelerationFrictionCoef = 0.95f
        ui.entityRatioChart.setCenterTextTypeface(MONOSPACE)
        ui.entityRatioChart.centerText = "Movies"
        ui.entityRatioChart.setCenterTextColor(WHITE)
        ui.entityRatioChart.isDrawHoleEnabled = true
        ui.entityRatioChart.setHoleColor(BLACK)
        ui.entityRatioChart.setTransparentCircleColor(WHITE)
        ui.entityRatioChart.setTransparentCircleAlpha(110)
        ui.entityRatioChart.holeRadius = 58f
        ui.entityRatioChart.transparentCircleRadius = 61f
        ui.entityRatioChart.setDrawCenterText(true)
        ui.entityRatioChart.rotationAngle = 0f
        ui.entityRatioChart.isRotationEnabled = true
        ui.entityRatioChart.isHighlightPerTapEnabled = true

//        ui.entityRatioChart.setOnChartValueSelectedListener(this)

        ui.entityRatioChart.animateY(1400, Easing.EaseInOutQuad)

        ui.entityRatioChart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            orientation = Legend.LegendOrientation.VERTICAL
            setDrawInside(false)
            textColor = WHITE
            xEntrySpace = 7f
            yEntrySpace = 0f
            yOffset = 0f
        }

        ui.entityRatioChart.setEntryLabelColor(BLACK)
        ui.entityRatioChart.setEntryLabelTypeface(MONOSPACE)
        ui.entityRatioChart.setEntryLabelTextSize(12f)
        setData(totalMovies, totalDocumentaries, totalBooks, totalGames)
    }

    private fun setData(totalMovies: Float, totalDocumentaries: Float, totalBooks: Float, totalGames: Float) {
        val entries = setOf(
            "Movies" to totalMovies,
            "Documentaries" to totalDocumentaries,
            "Games" to totalGames,
            "Books" to totalBooks
        ).map { PieEntry(it.second, it.first) }

        ui.entityRatioChart.data = PieDataSet(entries, "Total").apply {
            setDrawIcons(false)
            sliceSpace = 3f
            iconsOffset = MPPointF(0f, 40f)
            selectionShift = 5f
            colors = VORDIPLOM_COLORS.toList() + JOYFUL_COLORS.toList() + COLORFUL_COLORS.toList() +
                    LIBERTY_COLORS.toList() + PASTEL_COLORS.toList() + getHoloBlue()
        }.let(::PieData).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(11f)
            setValueTextColor(BLACK)
            setValueTypeface(MONOSPACE)
        }

        ui.entityRatioChart.highlightValues(null)
        ui.entityRatioChart.invalidate()
    }


    private fun setupViewModelSubscriptions() = databaseCounterCalculationViewModel.apply {
        totalAmount.observe(viewLifecycleOwner, ui.totalEntriesAmountTv::append)
        moviesAmount.observe(viewLifecycleOwner, ui.totalMoviesAmountTv::append)
        documentariesAmount.observe(viewLifecycleOwner, ui.totalDocumentariesAmountTv::append)
        booksAmount.observe(viewLifecycleOwner, ui.totalBooksAmountTv::append)
        gamesAmount.observe(viewLifecycleOwner, ui.totalGamesAmountTv::append)
    }
}
