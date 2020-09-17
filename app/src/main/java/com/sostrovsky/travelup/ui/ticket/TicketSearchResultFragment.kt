package com.sostrovsky.travelup.ui.ticket

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentTicketSearchResultBinding
import com.sostrovsky.travelup.domain.ticket.TicketDomain
import com.sostrovsky.travelup.ui.MenuFragment
import kotlinx.android.synthetic.main.fragment_ticket_search_result.*

/**
 * Fragment for displaying the result of the tickets search.
 */
class TicketSearchResultFragment : MenuFragment(R.layout.fragment_ticket_search_result) {
    private lateinit var mBinding: FragmentTicketSearchResultBinding
    private lateinit var viewModel: TicketViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(TicketViewModel::class.java)
        mBinding = (binding as FragmentTicketSearchResultBinding)

        viewModel.fetchTicketSearchResult().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { data ->
                setRecyclerView(data.first)
            })
    }

    private fun setRecyclerView(tickets: List<TicketDomain>) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager

        val adapter = TicketListAdapter(tickets as ArrayList<TicketDomain>)
        recyclerView.adapter = adapter
        adapter.notifyItemInserted(tickets.size - 1)
    }
}
