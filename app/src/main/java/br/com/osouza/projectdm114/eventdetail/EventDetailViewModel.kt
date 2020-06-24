package br.com.osouza.projectdm114.eventdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.osouza.projectdm114.persistence.OrderEvent
import br.com.osouza.projectdm114.persistence.OrderEventRepository

class EventDetailViewModel(orderEventId: String?) : ViewModel() {
    var orderEvent = MutableLiveData<OrderEvent>()

    init {
        if (orderEventId != null) {
            getOrderEvent(orderEventId)
        } else {
            orderEvent = MutableLiveData()
            orderEvent.value = OrderEvent()
        }
    }

    fun getOrderEvent(orderId: String) {
        orderEvent = OrderEventRepository.getOrderEventById(orderId)
    }

}