package br.com.osouza.projectdm114.eventdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.osouza.projectdm114.network.Product
import br.com.osouza.projectdm114.network.SalesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "ProductDetailViewModel"
class ProductDetailViewModel(private val code: String): ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product
    init {
        getProduct()
    }

    private fun getProduct() {
        Log.i(TAG, "Preparing to request a product by its code")
        coroutineScope.launch {
            var getProductDeferred = SalesApi.retrofitService.getProductByCode(code)
            try {
                Log.i(TAG, "Loading product by its code")
                var productByCode = getProductDeferred.await()
                Log.i(TAG, "Name of the product ${productByCode.name}")
                _product.value = productByCode
            } catch (e: Exception) {
                Log.i(TAG, "Error: ${e.message}")
            }
        }
        Log.i(TAG, "Product requested by code")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}