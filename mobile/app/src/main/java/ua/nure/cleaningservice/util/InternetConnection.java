package ua.nure.cleaningservice.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.annotation.NonNull;

public class InternetConnection {

    public static boolean checkConnection(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ((ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE)).getActiveNetwork() != null;
        } else {
            return true;
        }
    }
}
