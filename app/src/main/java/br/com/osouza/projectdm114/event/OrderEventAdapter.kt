package br.com.osouza.projectdm114.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.osouza.projectdm114.databinding.ItemEventBinding
import br.com.osouza.projectdm114.persistence.OrderEvent
import com.google.firebase.analytics.FirebaseAnalytics

class OrderEventAdapter(val orderEventClickListener: OrderEventClickListener) :
    ListAdapter<OrderEvent, OrderEventAdapter.OrderEventViewHolder>(OrderEventDiff) {
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderEventViewHolder {
        firebaseAnalytics = FirebaseAnalytics.getInstance(parent.context)
        return OrderEventViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: OrderEventViewHolder, position: Int) {
        val orderEvent = getItem(position)
        holder.bind(orderEvent)
        holder.itemView.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, orderEvent.code)
//            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
            orderEventClickListener.onClick(orderEvent)
        }
        holder.itemView.setOnLongClickListener {
//            val bundle = Bundle()
//            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, orderEvent.code)
//            firebaseAnalytics.logEvent("attempt_delete_product", bundle)
            true
        }
    }

    class OrderEventViewHolder(private var binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderEvent: OrderEvent) {
            binding.orderEvent = orderEvent
            binding.executePendingBindings()
        }
    }

    companion object OrderEventDiff : DiffUtil.ItemCallback<OrderEvent>() {
        override fun areItemsTheSame(oldItem: OrderEvent, newItem: OrderEvent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: OrderEvent, newItem: OrderEvent): Boolean {
            return ((oldItem.id == newItem.id)
                    && (oldItem.status.equals(newItem.status))
                    && (oldItem.userId.equals(newItem.userId))
                    && (oldItem.orderId == newItem.orderId))
        }
    }

    class OrderEventClickListener(val clickListener: (orderEvent: OrderEvent) -> Unit) {
        fun onClick(orderEvent: OrderEvent) = clickListener(orderEvent)
    }
}