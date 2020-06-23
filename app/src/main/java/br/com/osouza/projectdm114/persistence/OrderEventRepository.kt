package br.com.osouza.projectdm114.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "OrderEventRepository"
private const val COLLECTION = "orderEvents"
private const val FIELD_USER_ID = "userId"
private const val FIELD_ID = "id"
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

    fun getOrderEvents(): MutableLiveData<List<OrderEvent>> {
        val liveProducts = MutableLiveData<List<OrderEvent>>()
        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_USER_ID, firebaseAuth.uid)
            .orderBy(FIELD_DATE, Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val products = ArrayList<OrderEvent>()
                    querySnapshot.forEach {
                        val product = it.toObject<OrderEvent>()
                        product.id = it.id
                        products.add(product)
                    }
                    liveProducts.postValue(products)
                } else {
                    Log.d(TAG, "No product has been found")
                }
            }
        return liveProducts
    }

}