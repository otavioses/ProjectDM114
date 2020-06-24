package br.com.osouza.projectdm114.order

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.osouza.projectdm114.R
import br.com.osouza.projectdm114.databinding.FragmentOrderDetailBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.analytics.FirebaseAnalytics
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
        setHasOptionsMenu(true)
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
                                    orderDetailViewModel.orderDetail.value = it
                                }
                        }
                    }
                }
            }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_sign_out -> {
                this.context?.let {
                    AuthUI.getInstance()
                        .signOut(it)
                        .addOnCompleteListener {
                            this.activity?.recreate()
                        }
                }
                true
            }
            R.id.nav_event_list -> {
                val firebaseAnalytics = this.context?.let { FirebaseAnalytics.getInstance(it) }
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "")
                firebaseAnalytics?.logEvent("show_list_items", bundle)
                showEventList()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showEventList() {
        this.findNavController()
            .navigate(OrderDetailFragmentDirections.actionShowEventList())
    }


}