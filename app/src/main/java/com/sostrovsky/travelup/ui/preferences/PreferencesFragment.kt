package com.sostrovsky.travelup.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentPreferencesBinding
import kotlinx.android.synthetic.main.fragment_preferences.*

/**
 * Fragment that will help user to change its settings.
 */
class PreferencesFragment : Fragment() {
    lateinit var binding: FragmentPreferencesBinding
    lateinit var viewModel: PreferencesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_preferences, container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PreferencesViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.preferencesViewModel = viewModel
        setViewItems()
    }

    private fun setViewItems() {
        setUserSettings()
        setLanguageSpinner()
        setCurrencySpinner()
        setCountrySpinner()
        setSnackBar()
    }

    private fun setUserSettings() {
        viewModel.userSettingsLiveData.observe(viewLifecycleOwner, Observer { data ->
            if (data.isSet) {
                viewModel.userSettings = data
            }
        })
    }

    private fun setLanguageSpinner() {
        viewModel.userLanguagesLiveData.observe(viewLifecycleOwner, Observer { data ->
            spinnerLanguage.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, data
            )
            viewModel.userLanguages = data
        })

        viewModel.initComplete.observe(viewLifecycleOwner, Observer { initComplete ->
            if (initComplete) {
                viewModel.languagePositionLiveData.observe(viewLifecycleOwner, Observer { data ->
                    if (data >= 0) {
                        binding.spinnerLanguage.setSelection(data)
                    }
                })

                spinnerLanguage.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int,
                            id: Long
                        ) {
                            if (position >= 0) {
                                viewModel.setNewLanguage(position)
                            }
                        }
                    }
            }
        })
    }

    private fun setCurrencySpinner() {
        viewModel.userCurrenciesLiveData.observe(viewLifecycleOwner, Observer { data ->
            binding.spinnerCurrency.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, data
            )
            viewModel.userCurrencies = data
        })

        viewModel.initComplete.observe(viewLifecycleOwner, Observer { initComplete ->
            if (initComplete) {
                viewModel.currencyPositionLiveData.observe(viewLifecycleOwner, Observer { data ->
                    if (data >= 0) {
                        binding.spinnerCurrency.setSelection(data)
                    }
                })

                binding.spinnerCurrency.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int,
                            id: Long
                        ) {
                            viewModel.setNewCurrency(position)
                        }
                    }
            }
        })
    }

    private fun setCountrySpinner() {
        viewModel.userCountriesLiveData.observe(viewLifecycleOwner, Observer { data ->
            binding.spinnerCountry.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, data
            )
            viewModel.userCountries = data
        })

        viewModel.initComplete.observe(viewLifecycleOwner, Observer { initComplete ->
            if (initComplete) {
                viewModel.countryPositionLiveData.observe(viewLifecycleOwner, Observer { data ->
                    if (data >= 0) {
                        binding.spinnerCountry.setSelection(data)
                    }
                })

                binding.spinnerCountry.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int,
                            id: Long
                        ) {
                            viewModel.setNewCountry(position)
                        }
                    }
            }
        })
    }

    private fun setSnackBar() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            data ->
            if (data.first) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    data.second,
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.doneShowingSnackBar()
            }
        })
    }
}
