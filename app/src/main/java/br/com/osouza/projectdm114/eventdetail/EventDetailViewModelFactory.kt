package br.com.osouza.projectdm114.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EventDetailViewModelFactory(private val code: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(code) as T
        }
        throw IllegalArgumentException("The EventDetailViewModel class is required")
    }
}