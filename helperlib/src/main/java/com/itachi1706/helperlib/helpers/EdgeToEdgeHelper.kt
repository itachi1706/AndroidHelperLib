package com.itachi1706.helperlib.helpers

import android.os.Build
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("unused")
@RequiresApi(Build.VERSION_CODES.R)
object EdgeToEdgeHelper {

    @JvmStatic
    fun disableWindowSystemWindows(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    @JvmStatic
    fun setViewEdgeToEdge(view: android.view.View) {
        setViewEdgeToEdge(view, WindowInsetsCompat.Type.systemBars())
    }

    /**
     * Set the view to be edge to edge with a specific inset type
     * @param view The view to set the insets on
     * @param insetType The type of inset to apply from [WindowInsetsCompat.Type], e.g. [WindowInsetsCompat.Type.systemBars()]
     */
    @JvmStatic
    fun setViewEdgeToEdge(view: android.view.View, insetType: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val insets = windowInsets.getInsets(insetType)
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }
    }
}