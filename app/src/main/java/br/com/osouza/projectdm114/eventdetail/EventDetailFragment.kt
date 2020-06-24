package br.com.osouza.projectdm114.eventdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.osouza.projectdm114.R
import br.com.osouza.projectdm114.databinding.FragmentEventDetailBinding
import com.firebase.ui.auth.AuthUI

class EventDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        setHasOptionsMenu(true)

        if (this.arguments != null) {
            if (this.requireArguments().containsKey("orderEventId")) {
                val orderEventId = this.requireArguments().getString("orderEventId")
                val eventDetailViewModelFactory = EventDetailViewModelFactory(orderEventId)
                val eventDetailViewModel = ViewModelProvider(
                    this, eventDetailViewModelFactory
                ).get(EventDetailViewModel::class.java)

                binding.eventDetailViewModel = eventDetailViewModel
                eventDetailViewModel.orderEvent.observe(
                    viewLifecycleOwner,
                    Observer { orderEvent ->
                        orderEvent.productCode?.let {
                            val productDetailViewModel = ViewModelProviders.of(
                                this, ProductDetailViewModelFactory(it)
                            ).get(ProductDetailViewModel::class.java)
                            binding.productDetailViewModel = productDetailViewModel
                        }
                    })
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.event_details_menu, menu)
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

            else -> super.onOptionsItemSelected(item)
        }
    }
}
