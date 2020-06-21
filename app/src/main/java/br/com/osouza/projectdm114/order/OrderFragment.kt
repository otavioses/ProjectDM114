package br.com.osouza.projectdm114.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.osouza.projectdm114.databinding.FragmentOrderBinding
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class OrderFragment : Fragment() {
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider(this).get(OrderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOrderBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.orderViewModel = orderViewModel
        //Add the rest of the code here
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    orderViewModel.fcmRegistrationId.value = task.result?.token
                    Log.i("ProductInfoFragment", "FCM Token: ${task.result?.token}")
                    if (this.arguments != null) {
                        if (this.requireArguments().containsKey("productInfo")) {
                            val moshi = Moshi.Builder().build()
                            val jsonAdapter: JsonAdapter<Order> =
                                moshi.adapter<Order>(Order::class.java)
                            jsonAdapter.fromJson(this.requireArguments().getString("productInfo")!!).let {
                                orderViewModel.product.value = it
                            }
                        }
                    }
                }
            }


        return binding.root
    }
}