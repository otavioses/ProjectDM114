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

    fun getOrderEvent(orderEventId: String) {
        orderEvent = OrderEventRepository.getOrderEventById(orderEventId)
    }

    fun deleteOrderEvent() {
        orderEvent.value?.id?.let {
            OrderEventRepository.deleteProduct(it)
            orderEvent.value = null
        }
    }

    override fun onCleared() {
        if (orderEvent.value != null) {
            OrderEventRepository.saveOrderEvent(orderEvent.value!!)
        }
        super.onCleared()
    }

}