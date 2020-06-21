package br.com.osouza.projectdm114.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderDetailViewModel : ViewModel() {
    val fcmRegistrationId = MutableLiveData<String>()
    val orderDetail = MutableLiveData<OrderDetail>()
}