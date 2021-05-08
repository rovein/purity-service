package ua.nure.cleaningservice.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

object InternetConnection {
    fun checkConnection(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetwork != null
        } else {
            true
        }
    }
}
