package com.itachi1706.helperlib.helpers

import android.os.Build
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat

@Suppress("unused")
@RequiresApi(Build.VERSION_CODES.R)
object EdgeToEdgeHelper {

    @JvmStatic
    fun disableWindowSystemWindows(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    @JvmStatic
    fun setViewEdgeToEdge(view: android.view.View) {
        view.windowInsetsController?.let {
            it.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(android.view.WindowInsets.Type.systemBars())
        }
    }
}