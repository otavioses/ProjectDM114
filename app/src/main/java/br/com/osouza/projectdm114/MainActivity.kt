package br.com.osouza.projectdm114

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import br.com.osouza.projectdm114.order.OrderDetailFragmentDirections
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

private const val AUTHENTICATION_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val name = user.displayName
            val email = user.email
            Toast.makeText(this, "Sign in!\nUser: $name \nEmail: $email", Toast.LENGTH_SHORT).show()
            setFirebaseRemoteConfig()
            setContentView(R.layout.activity_main)
        } else {
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), AUTHENTICATION_REQUEST_CODE
            )
        }

        if (this.intent.hasExtra("orderDetail")) {
            showOrderDetail(intent.getStringExtra("orderDetail")!!)
        }
    }

    override fun onNewIntent(intent: Intent) {
        if (intent.hasExtra("orderDetail")) {
            showOrderDetail(intent.getStringExtra("orderDetail")!!)
        }
        super.onNewIntent(intent)
    }

    private fun showOrderDetail(orderId: String) {
        this.findNavController(R.id.nav_host_fragment)
            .navigate(OrderDetailFragmentDirections.actionShowOrderInfo(orderId))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHENTICATION_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                setFirebaseRemoteConfig()
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val name = user.displayName
                    val email = user.email
                    Toast.makeText(
                        this,
                        "Sign in!\nUser: $name \nEmail: $email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setFirebaseRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        val defaultConfigMap: MutableMap<String, Any> = HashMap()
        defaultConfigMap["delete_detail_view"] = true
        defaultConfigMap["delete_list_view"] = false
        remoteConfig.setDefaultsAsync(defaultConfigMap)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("MainActivity", "Remote config updated: $updated")
                } else {
                    Log.d("MainActivity", "Failed to load remote config")
                }
            }
    }

}