package com.sostrovsky.travelup.ui.ticket

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.domain.ticket.TicketDomain
import com.sostrovsky.travelup.util.inflate
import kotlinx.android.synthetic.main.tickets_item_row.view.*

/**
 * Author: Sergey Ostrovsky
 * Date: 02.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class TicketListAdapter(private val tickets: ArrayList<TicketDomain>) :
    RecyclerView.Adapter<TicketListAdapter.TicketHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketListAdapter.TicketHolder {
        val inflatedView = parent.inflate(R.layout.tickets_item_row, false)
        return TicketHolder(inflatedView)
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(holder: TicketListAdapter.TicketHolder, position: Int) {
        val itemTicket = tickets[position]
        holder.bindTicket(itemTicket)
    }

    class TicketHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var ticket: TicketDomain? = null

        fun bindTicket(ticket: TicketDomain) {
            this.ticket = ticket
            view.lblDepartureDate.text = (view.context.getText(R.string.label_departure_date)
                    as String).format(view.context.getText(R.string.departure_date))
            view.departureDate.text = ticket.departureDate
            view.departureFromTo.text = ticket.departureFromTo
            view.departureTime.text = ticket.departureTime
            view.carrierName.text = ticket.carrierName
            view.flightPrice.text = ticket.flightPrice
        }
    }
}