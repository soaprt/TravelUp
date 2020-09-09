package com.sostrovsky.travelup.ui.ticket.search_result

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sostrovsky.travelup.ui.MenuFragment
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentSearchTicketResultBinding
import timber.log.Timber

/**
 * Fragment for displaying the result of the tickets search.
 */
class TicketSearchResultFragment : MenuFragment(R.layout.fragment_search_ticket_result) {
    private lateinit var mBinding: FragmentSearchTicketResultBinding
    private lateinit var viewModel: TicketSearchResultViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TicketSearchResultViewModel::class.java)

        mBinding = (binding as FragmentSearchTicketResultBinding)

        viewModel.ticketSearchResult.observe(viewLifecycleOwner, Observer { data ->
            Timber.e("TicketSearchResultFragment: ticketSearchResult: $data")
        })
    }
}
