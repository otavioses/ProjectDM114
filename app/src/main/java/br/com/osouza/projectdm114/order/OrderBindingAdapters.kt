package br.com.osouza.projectdm114.order

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("orderId")
fun bindOrderId(txtOrderId: TextView, orderId: Int?) {
    txtOrderId?.let {
        txtOrderId.text = "$orderId"
    }
}