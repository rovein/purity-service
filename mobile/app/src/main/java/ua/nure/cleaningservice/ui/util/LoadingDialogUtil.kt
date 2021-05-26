package ua.nure.cleaningservice.ui.util

import android.app.Activity
import android.app.AlertDialog
import ua.nure.cleaningservice.R

class LoadingDialogUtil(private val activity: Activity) {

    private lateinit var dialog: AlertDialog
    private var progressStatus = 0

    fun start() {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
