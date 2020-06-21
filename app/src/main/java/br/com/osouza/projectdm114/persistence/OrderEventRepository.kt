package br.com.osouza.projectdm114.persistence

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "OrderEventRepository"
private const val COLLECTION = "orderEvents"
private const val FIELD_USER_ID = "userId"
private const val FIELD_ORDER_ID = "orderId"
private const val FIELD_STATUS = "status"
private const val FIELD_DATE = "date"

object OrderEventRepository {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun saveOrderEvent(orderEvent: OrderEvent): String {
        val document = if (orderEvent.id != null) {
            firebaseFirestore.collection(COLLECTION).document(orderEvent.id!!)
        } else {
            orderEvent.userId = firebaseAuth.uid!!
            firebaseFirestore.collection(COLLECTION).document()
        }
        document.set(orderEvent)
        return document.id
    }
}