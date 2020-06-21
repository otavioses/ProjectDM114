package br.com.osouza.projectdm114.persistence

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class OrderEvent(
    @Exclude var id: String? = null,
    var userId: String? = null,
    var orderId: String? = null,
    var date: Date? = null,
    var status: String? = null
)