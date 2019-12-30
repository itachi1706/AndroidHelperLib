package com.itachi1706.helperlib.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.helpers in Helper Library
 * NOTE: Requires android.permission.ACCESS_NETWORK_STATE permission
 */
object ConnectivityHelper {
    const val NO_CONNECTION = -1

    /**
     * Gets the Network Info Object
     * @param context Context
     * @return Network Info
     */
    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = getConnectivityManager(context)
        return cm.activeNetworkInfo
    }

    private fun getNetworkType(networkInfo: NetworkInfo?): Int {
        return networkInfo?.type ?: NO_CONNECTION
    }

    /**
     * Gets the Connectivity Manager object
     * @param context Context
     * @return Connectivity Manager
     */
    private fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * Check if the android device has Internet
     * @param context Context
     * @return True if internet, false otherwise
     */
    fun hasInternetConnection(context: Context): Boolean {
        val activeNetwork = getNetworkInfo(context)
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    /**
     * Check if connection is a WIFI connection
     * @param context Context
     * @return True if WIFI, false otherwise
     */
    fun isWifiConnection(context: Context): Boolean {
        val activeNetwork = getNetworkInfo(context)
        return activeNetwork != null && getNetworkType(activeNetwork) == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Check if connection is Mobile Data
     * @param context Context
     * @return True if Cellular (Mobile), false otherwise
     */
    fun isCellularConnection(context: Context): Boolean {
        val activeNetwork = getNetworkInfo(context)
        return activeNetwork != null && getNetworkType(activeNetwork) == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * Get the current active network type
     * @param context Context
     * @return Active Netwrok Type (ConnectivityManager.TYPE_WIFI etc.)
     */
    fun getActiveNetworkType(context: Context): Int {
        return getNetworkType(getNetworkInfo(context))
    }

    /**
     * Whether the app should throttle usage of data
     * This is really for devices running API 24 (Nougat) where there is the Data Saver option
     * @param context Context
     * @return true if it should be throttled, false otherwise
     */
    fun shouldThrottle(context: Context): Boolean {
        val manager = getConnectivityManager(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isActiveNetworkMetered) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    when (manager.restrictBackgroundStatus) {
                        ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED -> true
                        ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED, ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED -> false
                        else -> false
                    }
                } else false
            } else false
        } else false
    }

    /**
     * Gets the Data Saver Option for the application itself
     * @param context Context
     * @return int One of 3 choices
     * {RESTRICT_BACKGROUND_STATUS_ENABLED, RESTRICT_BACKGROUND_STATUS_WHITELISTED, RESTRICT_BACKGROUND_STATUS_DISABLED}
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun getDataSaverSetting(context: Context): Int {
        val manager = getConnectivityManager(context)
        return manager.restrictBackgroundStatus
    }
}