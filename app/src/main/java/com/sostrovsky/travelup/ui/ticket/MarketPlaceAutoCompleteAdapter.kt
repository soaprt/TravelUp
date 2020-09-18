package com.sostrovsky.travelup.ui.ticket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.domain.ticket.MarketPlaceDomain
import kotlinx.android.synthetic.main.autocomplete_list_row.view.*

/**
 * Author: Sergey Ostrovsky
 * Date: 17.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class MarketPlaceAutoCompleteAdapter(
    context: Context,
    val marketPlaces: MutableList<MarketPlaceDomain>
) : ArrayAdapter<MarketPlaceDomain>(context, 0, marketPlaces) {

    var marketPlacesAll = mutableListOf<MarketPlaceDomain>()

    override fun getFilter(): Filter {
        return marketPlaceFilter
    }

    override fun getCount(): Int = marketPlacesAll.size

    override fun getItem(position: Int): MarketPlaceDomain = marketPlacesAll[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.autocomplete_list_row, parent, false
        )
        view.label.text = marketPlacesAll[position].name
        return view
    }

    private val marketPlaceFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions = mutableListOf<MarketPlaceDomain>()

            if (constraint == null || constraint.isEmpty()) {
                suggestions.addAll(marketPlaces)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()

                marketPlaces.forEach {
                    if (it.name.toLowerCase().contains(filterPattern)) {
                        suggestions.add(it)
                    }
                }
            }

            results.values = suggestions
            results.count = suggestions.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null) {
                marketPlacesAll.clear()
                marketPlacesAll.addAll(results.values as List<MarketPlaceDomain>)
                notifyDataSetChanged()
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as MarketPlaceDomain).name
        }
    }
}