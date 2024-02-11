package dev.liinahamari.summary_ui

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
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
    }

    private fun setupViewModelSubscriptions() = databaseCounterCalculationViewModel.apply {
        totalAmount.observe(viewLifecycleOwner, ui.totalEntriesAmountTv::append)
        moviesAmount.observe(viewLifecycleOwner, ui.totalMoviesAmountTv::append)
        documentariesAmount.observe(viewLifecycleOwner, ui.totalDocumentariesAmountTv::append)
        booksAmount.observe(viewLifecycleOwner, ui.totalBooksAmountTv::append)
        gamesAmount.observe(viewLifecycleOwner, ui.totalGamesAmountTv::append)
    }
}
