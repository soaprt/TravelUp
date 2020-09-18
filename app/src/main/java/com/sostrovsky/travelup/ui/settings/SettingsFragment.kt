package com.sostrovsky.travelup.ui.settings

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
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentSettingsBinding
import com.sostrovsky.travelup.ui.ticket.TicketActivity
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * Fragment that will help user to change its settings.
 */
class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.settingsViewModel = viewModel
        setViewItems()
    }

    private fun setViewItems() {
        setSpinnerLanguage()
        setCurrencySpinner()
        setCountrySpinner()
        setSnackBar()
    }

    private fun setSpinnerLanguage() {
        viewModel.fetchLanguages().observe(viewLifecycleOwner, Observer { data ->
            spinnerLanguage.adapter = ArrayAdapter(
                requireContext(), R.layout.spinner_selected_item,
                data.first
            ).apply {
                setDropDownViewResource(R.layout.spinner_list_item)
            }

            val itemToSelect = viewModel.getLastListItem(data.second)
            if (itemToSelect >= 0) {
                spinnerLanguage.setSelection(itemToSelect)
            }
        })

        spinnerLanguage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int,
                    id: Long
                ) {
                    if (view != null && position >= 0) {
                        viewModel.setNewLanguage(position)
                    }
                }
            }
    }

    private fun setCurrencySpinner() {
        viewModel.fetchCurrencies().observe(viewLifecycleOwner, Observer { data ->
            spinnerCurrency.adapter = ArrayAdapter(
                requireContext(), R.layout.spinner_selected_item,
                data.first
            ).apply {
                setDropDownViewResource(R.layout.spinner_list_item)
            }

            val itemToSelect = viewModel.getLastListItem(data.second)
            if (itemToSelect >= 0) {
                spinnerCurrency.setSelection(itemToSelect)
            }
        })

        spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int,
                    id: Long
                ) {
                    if (view != null && position >= 0) {
                        viewModel.setNewCurrency(position)
                    }
                }
            }
    }

    private fun setCountrySpinner() {
        viewModel.fetchCountries().observe(viewLifecycleOwner, Observer { data ->
            spinnerCountry.adapter = ArrayAdapter(
                requireContext(), R.layout.spinner_selected_item,
                data.first
            ).apply {
                setDropDownViewResource(R.layout.spinner_list_item)
            }

            val itemToSelect = viewModel.getLastListItem(data.second)
            if (itemToSelect >= 0) {
                spinnerCountry.setSelection(itemToSelect)
            }
        })

        spinnerCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int,
                    id: Long
                ) {
                    if (view != null && position >= 0) {
                        viewModel.setNewCountry(position)
                    }
                }
            }
    }

    private fun setSnackBar() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer { data ->
            if (data.first) {
                (activity as TicketActivity).showSnackBarEvent(data.second)
                viewModel.doneShowingSnackBar()
            }
        })
    }
}
