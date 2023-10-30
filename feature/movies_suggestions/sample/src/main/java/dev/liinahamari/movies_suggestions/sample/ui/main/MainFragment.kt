package dev.liinahamari.movies_suggestions.sample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.liinahamari.movies_suggestions.sample.R


class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.sendRequestBtn).setOnClickListener {
            viewModel.searchForMovie(view.findViewById<EditText>(R.id.enterMovieNameEt).text.toString())
        }
        setupViewModelSubscriptions()
    }

    private fun setupViewModelSubscriptions() {
        viewModel.searchMoviesEvent.observe(viewLifecycleOwner) {
            when (it) {
                is GetRemoteMovies.Error -> Toast.makeText(requireContext(), "Search failed", Toast.LENGTH_SHORT).show()
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
