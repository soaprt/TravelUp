package com.sostrovsky.travelup.ui.ticket

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentTicketSearchBinding
import com.sostrovsky.travelup.ui.MenuFragment
import kotlinx.android.synthetic.main.fragment_ticket_search.*
import java.util.*

/**
 * Fragment for searching tickets.
 */
class TicketSearchFragment : MenuFragment(R.layout.fragment_ticket_search) {
    private lateinit var mBinding: FragmentTicketSearchBinding
    private lateinit var viewModel: TicketViewModel

    lateinit var marketPlaceAdapter: MarketPlaceAutoCompleteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(TicketViewModel::class.java)

        mBinding = (binding as FragmentTicketSearchBinding)
        mBinding.setLifecycleOwner(this)
        mBinding.searchViewModel = viewModel

        setViewItems()
    }

    private fun setViewItems() {
        setMarketPlaceAdapter()
        setPlaceFrom()
        setPlaceTo()
        setDepartureDate()
        setSearchButton()
    }

    private fun setMarketPlaceAdapter() {
        marketPlaceAdapter = MarketPlaceAutoCompleteAdapter(requireContext(), mutableListOf())
        viewModel.fetchMarketPlaces().observe(viewLifecycleOwner, Observer { data ->
            marketPlaceAdapter.marketPlaces.clear()
            marketPlaceAdapter.marketPlaces.addAll(data)
            marketPlaceAdapter.notifyDataSetChanged()
        })
    }

    private fun setPlaceFrom() {
        edtPlaceFrom.apply {
            setText(viewModel.fetchPlaceFrom())
            setAdapter(marketPlaceAdapter)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(text: Editable) {}
                override fun beforeTextChanged(
                    text: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    text: CharSequence, start: Int, before: Int, count: Int
                ) {
                    val setResult = viewModel.setPlaceFrom(text)
                    edtPlaceFrom.error = setResult
                }
            })
        }
    }

    private fun setPlaceTo() {
        edtPlaceTo.apply {
            setText(viewModel.fetchPlaceTo())
            setAdapter(marketPlaceAdapter)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {}
                override fun beforeTextChanged(
                    text: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    text: CharSequence?, start: Int, before: Int, count: Int
                ) {
                    val setResult = viewModel.setPlaceTo(text)
                    edtPlaceTo.error = setResult
                }
            })
        }
    }

    private fun setDepartureDate() {
        edtDepartureDate.apply {
            viewModel.fetchDepartureDate().observe(viewLifecycleOwner, Observer { data ->
                setText(data)
            })

            setOnClickListener {
                val selectedDate = Calendar.getInstance()

                DatePickerDialog(
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        val setResult = viewModel.setDepartureDate(selectedDate)
                        error = setResult
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun setSearchButton() {
        btnSearch.setOnClickListener {
            (requireActivity() as TicketActivity).hideSoftKeyboard(requireActivity())
            viewModel.searchTicket()
        }

        /*
         * Receives the Triple, where:
         *  first = List<TicketDomainModel>
         *  second = action (where to move if the list is not empty)
         *  third = error message (if the list is empty)
         */
        viewModel.fetchTicketSearchResult().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { data ->
                if (viewModel.canMoveToResults) {
                    if (data.first.isNotEmpty()) {
                        viewModel.canMoveToResults = false
                        (activity as TicketActivity).moveTo(data.second)
                    } else {
                        (activity as TicketActivity).showSnackBarEvent(data.third)
                    }
                }
            })
    }
}