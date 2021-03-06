package com.romeo.core.data.api

import android.content.Context
import android.net.ConnectivityManager


class NetworkManagerImpl(
    private val context: Context
) : NetworkManager {
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}