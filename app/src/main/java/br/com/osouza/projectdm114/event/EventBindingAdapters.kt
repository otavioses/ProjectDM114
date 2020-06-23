package br.com.osouza.projectdm114.event

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.osouza.projectdm114.Utils.Util
import br.com.osouza.projectdm114.persistence.OrderEvent
import java.util.*

@BindingAdapter("orderEventList")
fun bindOrderEventsList(recyclerView: RecyclerView, orderEvents: List<OrderEvent>?) {
    val adapter = recyclerView.adapter as OrderEventAdapter
    adapter.submitList(orderEvents)
}

@BindingAdapter("orderEventDate")
fun bindOrderEventDate(txtProductPrice: TextView, date: Date?) {
    date?.let {
        txtProductPrice.text = Util.getDateFormated(it)
    }
}