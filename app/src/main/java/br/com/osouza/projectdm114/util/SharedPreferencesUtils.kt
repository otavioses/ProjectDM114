package br.com.osouza.projectdm114.util

import android.content.Context
import android.content.SharedPreferences
import br.com.osouza.projectdm114.MainApplication

private const val ACCESS_TOKEN = "access_token"
private const val EXPIRES_IN = "expires_in"

object SharedPreferencesUtils {
    private fun getSharedPreferences(): SharedPreferences {
        val context = MainApplication.getApplicationContext()
        return context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }

    fun saveToken(accessToken: String, expiresIn: Int) {
        with(getSharedPreferences().edit()) {
            putString(ACCESS_TOKEN, accessToken)
            putInt(EXPIRES_IN, ((System.currentTimeMillis() / 1000) + expiresIn).toInt())
            commit()
        }
    }

    fun removeToken(accessToken: String, expiresIn: Int) {
        with(getSharedPreferences().edit()) {
            remove(ACCESS_TOKEN)
            remove(EXPIRES_IN)
            commit()
        }
    }

    fun getAccessToken(): String? {
        var accessToken: String? = null
        with(getSharedPreferences()) {
            if (contains(ACCESS_TOKEN) && contains(EXPIRES_IN)) {
                val expiresIn = getInt(EXPIRES_IN, 0)
                if (expiresIn > (System.currentTimeMillis() / 1000)) {
                    accessToken = getString(ACCESS_TOKEN, "")
                }
            }
        }
        return accessToken
    }

}