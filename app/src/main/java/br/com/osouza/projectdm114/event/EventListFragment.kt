package br.com.osouza.projectdm114.event

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.osouza.projectdm114.R
import br.com.osouza.projectdm114.databinding.FragmentEventListBinding
import br.com.osouza.projectdm114.persistence.OrderEvent
import com.firebase.ui.auth.AuthUI

class EventListFragment : Fragment() {
    private val eventListViewModel: EventListViewModel by lazy {
        ViewModelProvider(this).get(EventListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventListBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.eventListViewModel = eventListViewModel
        setHasOptionsMenu(true)

        val itemDecor = DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        binding.rcvOrderEvents.addItemDecoration(itemDecor);

        binding.rcvOrderEvents.adapter = OrderEventAdapter(OrderEventAdapter.OrderEventClickListener {
            it.id?.let { it1 -> showEventDetail(it1) }
        })

        binding.orderEventRefresh.setOnRefreshListener {
            eventListViewModel.refreshProducts()
            binding.orderEventRefresh.isRefreshing = false
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.event_list_menu, menu)
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

    private fun showEventDetail(orderEventId: String) {
        this.findNavController()
            .navigate(EventListFragmentDirections.actionShowEventDetail(orderEventId))
    }

}
