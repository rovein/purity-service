package ua.nure.cleaningservice.ui.util

import android.app.Activity
import android.app.AlertDialog
import android.text.InputType
import android.widget.EditText

class AlertDialogUtil {

    companion object AlertDialogUtil {
        fun getBaseAlertDialogBuilder(input: EditText, context: Activity): AlertDialog.Builder {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)

            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            return builder
        }
    }

}
