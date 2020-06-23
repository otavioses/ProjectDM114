package br.com.osouza.projectdm114.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.osouza.projectdm114.MainActivity
import br.com.osouza.projectdm114.R
import br.com.osouza.projectdm114.Utils.Util
import br.com.osouza.projectdm114.order.OrderDetail
import br.com.osouza.projectdm114.persistence.OrderEvent
import br.com.osouza.projectdm114.persistence.OrderEventRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

private const val TAG = "FCMService"

class FCMService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Payload: " + remoteMessage.data)

            if (remoteMessage.data.containsKey("orderDetail")) {
                val moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<OrderDetail> =
                    moshi.adapter<OrderDetail>(OrderDetail::class.java)
                jsonAdapter.fromJson(remoteMessage.data["orderDetail"]!!).let {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null && it != null) {
                        if (it.username == user.email)
                            OrderEventRepository.saveOrderEvent(OrderEvent(
                                orderId = it.orderId,
                                status = it.status,
                                date = Util.getCurrentDate(),
                                productCode = it.productCode
                            ))

                        sendProductNotification(remoteMessage.data["orderDetail"]!!)
                    }
                }


            }
        }
    }

    private fun sendProductNotification(productInfo: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("orderDetail", productInfo)
        sendNotification(intent)
    }

    private fun sendNotification(intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channelId = "1"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_cloud_queue_black_24dp)
            .setContentTitle("Sales Message")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sales provider",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}