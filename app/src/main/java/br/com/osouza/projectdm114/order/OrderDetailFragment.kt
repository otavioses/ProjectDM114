package br.com.osouza.projectdm114.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.osouza.projectdm114.databinding.FragmentOrderDetailBinding
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class OrderDetailFragment : Fragment() {
    private val orderDetailViewModel: OrderDetailViewModel by lazy {
        ViewModelProvider(this).get(OrderDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOrderDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.orderDetailViewModel = orderDetailViewModel
        //Add the rest of the code here
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    orderDetailViewModel.fcmRegistrationId.value = task.result?.token
                    Log.i("ProductInfoFragment", "FCM Token: ${task.result?.token}")
                    if (this.arguments != null) {
                        if (this.requireArguments().containsKey("productInfo")) {
                            val moshi = Moshi.Builder().build()
                            val jsonAdapter: JsonAdapter<OrderDetail> =
                                moshi.adapter<OrderDetail>(OrderDetail::class.java)
                            jsonAdapter.fromJson(this.requireArguments().getString("productInfo")!!)
                                .let {
                                    orderDetailViewModel.product.value = it
                                }
                        }
                    }
                }
            }
        
        return binding.root
    }
}