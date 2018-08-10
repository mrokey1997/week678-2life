package gggroup.com.baron.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtil {
    companion object {
        @Suppress("DEPRECATION")
        fun isOnline(context: Context) : Boolean {
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}