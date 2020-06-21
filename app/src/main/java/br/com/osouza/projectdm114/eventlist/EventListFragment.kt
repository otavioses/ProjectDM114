package br.com.osouza.projectdm114.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.osouza.projectdm114.databinding.FragmentEventListBinding
import br.com.osouza.projectdm114.databinding.FragmentOrderDetailBinding

class EventListFragment : Fragment() {
//    private val orderDetailViewModel: OrderDetailViewModel by lazy {
//        ViewModelProvider(this).get(OrderDetailViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventListBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
//        binding.orderDetailViewModel = orderDetailViewModel
//        //Add the rest of the code here
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    orderDetailViewModel.fcmRegistrationId.value = task.result?.token
//                    Log.i("ProductInfoFragment", "FCM Token: ${task.result?.token}")
//                    if (this.arguments != null) {
//                        if (this.requireArguments().containsKey("productInfo")) {
//                            val moshi = Moshi.Builder().build()
//                            val jsonAdapter: JsonAdapter<OrderDetail> =
//                                moshi.adapter<OrderDetail>(OrderDetail::class.java)
//                            jsonAdapter.fromJson(this.requireArguments().getString("productInfo")!!)
//                                .let {
//                                    orderDetailViewModel.orderDetail.value = it
//                                }
//                        }
//                    }
//                }
//            }

        return binding.root
    }
}
