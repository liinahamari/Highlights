package dev.liinahamari.suggestions.sample.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.liinahamari.core.ext.toast
import dev.liinahamari.suggestions.sample.R


class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.enterMovieNameEt).doOnTextChanged { text, _, _, _ ->
            viewModel.searchForMovie(text.toString())
        }
        setupViewModelSubscriptions()
    }

    private fun setupViewModelSubscriptions() {
        viewModel.searchMoviesEvent.observe(viewLifecycleOwner) {
            when (it) {
                is GetRemoteMovies.Error -> toast("Suggestions API failed")

                is GetRemoteMovies.Success -> {
                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line, it.titles
                    )
                    requireView().findViewById<AutoCompleteTextView>(R.id.enterMovieNameEt).setAdapter(adapter)
                }
            }
        }
    }
}
