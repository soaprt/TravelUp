package com.sostrovsky.travelup.ui.ticket

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentSearchTicketResultBinding
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.ui.MenuFragment
import kotlinx.android.synthetic.main.fragment_search_ticket_result.*

/**
 * Fragment for displaying the result of the tickets search.
 */
class TicketSearchResultFragment : MenuFragment(R.layout.fragment_search_ticket_result) {
    private lateinit var mBinding: FragmentSearchTicketResultBinding
    private lateinit var viewModel: TicketViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(TicketViewModel::class.java)
        mBinding = (binding as FragmentSearchTicketResultBinding)

        viewModel.ticketSearchResult.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { data ->
                setRecyclerView(data.first)
            })
    }

    private fun setRecyclerView(tickets: List<TicketDomainModel>) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager

        val adapter = TicketListAdapter(tickets as ArrayList<TicketDomainModel>)
        recyclerView.adapter = adapter
        adapter.notifyItemInserted(tickets.size - 1)
    }
}
