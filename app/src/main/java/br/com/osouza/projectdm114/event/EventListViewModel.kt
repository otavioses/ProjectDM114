package br.com.osouza.projectdm114.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.osouza.projectdm114.persistence.OrderEvent
import br.com.osouza.projectdm114.persistence.OrderEventRepository

class EventListViewModel() : ViewModel() {
    var orderEvent = MutableLiveData<List<OrderEvent>>()

    init {
        getOrderEvents()
    }

    fun getOrderEvents() {
        orderEvent = OrderEventRepository.getOrderEvents()
    }

    fun saveOrderEvent(orderEvent: OrderEvent) {
        OrderEventRepository.saveOrderEvent(orderEvent)
        getOrderEvents()
    }
}
