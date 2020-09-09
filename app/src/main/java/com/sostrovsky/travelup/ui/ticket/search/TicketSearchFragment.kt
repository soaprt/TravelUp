package com.sostrovsky.travelup.ui.ticket.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentSearchTicketBinding
import com.sostrovsky.travelup.ui.MenuFragment
import kotlinx.android.synthetic.main.fragment_search_ticket.*
import timber.log.Timber
import java.util.*

/**
 * Fragment for searching tickets.
 */
class TicketSearchFragment : MenuFragment(R.layout.fragment_search_ticket) {
    private lateinit var mBinding: FragmentSearchTicketBinding
    private lateinit var viewModel: TicketSearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TicketSearchViewModel::class.java)

        mBinding = (binding as FragmentSearchTicketBinding)
        mBinding.setLifecycleOwner(this)
        mBinding.searchViewModel = viewModel

        viewModel.userSettings.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data ->
            viewModel.placeSearchParams.userSettings = data
            viewModel.ticketSearchParams.userSettings = data
        })

        setViewItems()
    }

    private fun setViewItems() {
        setDestinationFrom()
        setFlyingTo()
        setDepartureDate()
        setSearchButton()
    }

    private fun setDestinationFrom() {
        edtDestinationFrom.apply {
            setText(viewModel.ticketSearchParams.destinationFrom)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(destinationFrom: Editable) {}
                override fun beforeTextChanged(
                    destinationFrom: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    destinationFrom: CharSequence, start: Int, before: Int, count: Int
                ) {
                    val setResult = viewModel.setDestinationFrom(destinationFrom)
                    edtDestinationFrom.error = setResult
                }
            })
        }
    }

    private fun setFlyingTo() {
        edtFlyingTo.apply {
            setText(viewModel.ticketSearchParams.flyingTo)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(flyingTo: Editable?) {}
                override fun beforeTextChanged(
                    flyingTo: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    flyingTo: CharSequence?, start: Int, before: Int, count: Int
                ) {
                    val setResult = viewModel.setFlyingTo(flyingTo)
                    edtFlyingTo.error = setResult
                }
            })
        }
    }

    private fun setDepartureDate() {
        viewModel.departureDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data ->
            edtDepartureDate.setText(data)
        })

        edtDepartureDate.setOnClickListener {
            val selectedDate = Calendar.getInstance()

            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val setResult = viewModel.setDepartureDate(selectedDate)
                    edtDepartureDate.error = setResult
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setSearchButton() {
        viewModel.ticketSearchResult.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { data ->
                Timber.e("SearchFragment: ticketSearchResult: $data")
            })
    }
}