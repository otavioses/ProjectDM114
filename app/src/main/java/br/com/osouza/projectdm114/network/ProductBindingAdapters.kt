package br.com.osouza.projectdm114.network

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("productPrice")
fun bindProductPrice(txtProductPrice: TextView, productPrice: Double?) {
    productPrice?.let {
        val price = "$ " + "%.2f".format(productPrice)
        txtProductPrice.text = price
    }
}