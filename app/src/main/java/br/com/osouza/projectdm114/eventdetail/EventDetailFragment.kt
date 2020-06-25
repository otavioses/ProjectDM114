package br.com.osouza.projectdm114.eventdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.osouza.projectdm114.R
import br.com.osouza.projectdm114.databinding.FragmentEventDetailBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

class EventDetailFragment : Fragment() {

    lateinit var eventDetailViewModel: EventDetailViewModel
    var orderEventId: String? = null
    var hasDeleteDetailMenu = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val remoteConfig = Firebase.remoteConfig
        hasDeleteDetailMenu = remoteConfig.getBoolean("delete_detail_view")
        setHasOptionsMenu(true)

        if (this.arguments != null) {
            if (this.requireArguments().containsKey("orderEventId")) {
                val orderEventId = this.requireArguments().getString("orderEventId")
                this.orderEventId = orderEventId
                val eventDetailViewModelFactory = EventDetailViewModelFactory(orderEventId)
                eventDetailViewModel = ViewModelProvider(
                    this, eventDetailViewModelFactory
                ).get(EventDetailViewModel::class.java)

                binding.eventDetailViewModel = eventDetailViewModel
                eventDetailViewModel.orderEvent.observe(
                    viewLifecycleOwner,
                    Observer { orderEvent ->
                        if (orderEvent != null) {
                            orderEvent.productCode?.let {
                                val productDetailViewModel = ViewModelProvider(
                                    this, ProductDetailViewModelFactory(it)
                                ).get(ProductDetailViewModel::class.java)
                                binding.productDetailViewModel = productDetailViewModel
                            }
                        }
                    })
            }
        }

        val firebaseAnalytics = this.context?.let { FirebaseAnalytics.getInstance(it) }
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "")
        firebaseAnalytics?.logEvent("show_event_details", bundle)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if (hasDeleteDetailMenu) {
            inflater.inflate(R.menu.event_details_menu, menu)
        } else {
            inflater.inflate(R.menu.event_list_menu, menu)
        }
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
            R.id.nav_delete -> {
                eventDetailViewModel.deleteOrderEvent()

                val firebaseAnalytics = this.context?.let { FirebaseAnalytics.getInstance(it) }
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, orderEventId)
                firebaseAnalytics?.logEvent("delete_event_details", bundle)

                findNavController().popBackStack()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
