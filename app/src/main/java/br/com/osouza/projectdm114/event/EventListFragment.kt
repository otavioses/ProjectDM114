package br.com.osouza.projectdm114.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.osouza.projectdm114.databinding.FragmentEventListBinding

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

        val itemDecor = DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        binding.rcvOrderEvents.addItemDecoration(itemDecor);

        binding.rcvOrderEvents.adapter = OrderEventAdapter(OrderEventAdapter.OrderEventClickListener {

        })

        binding.orderEventRefresh.setOnRefreshListener {
            eventListViewModel.refreshProducts()
            binding.orderEventRefresh.isRefreshing = false
        }

        return binding.root
    }
}
