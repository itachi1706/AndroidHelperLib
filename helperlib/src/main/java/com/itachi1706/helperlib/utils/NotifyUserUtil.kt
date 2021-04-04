package com.itachi1706.helperlib.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.itachi1706.helperlib.R

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.utils in Helper Library
 */
object NotifyUserUtil {
    /**
     * Creates a short dismissable snackbar object
     * @param currentLayout Current View Layout
     * @param message Message in Snackbar
     */
    @JvmStatic
    fun showShortDismissSnackbar(currentLayout: View, message: CharSequence) {
        Snackbar.make(currentLayout, message, Snackbar.LENGTH_SHORT)
            .setAction(R.string.snackbar_action_dismiss) { }.show()
    }

    /**
     * Create a Short Toast Message
     * @param context Application Context
     * @param message Message to display in Toast
     */
    @JvmStatic
    fun createShortToast(context: Context, message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}