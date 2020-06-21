package br.com.osouza.projectdm114.order

data class Order(
    var username: String,
    var orderId: Int,
    var status: String,
    var productCode: String
)