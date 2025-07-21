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

    /**
     * Disable the window system windows to allow edge to edge layout
     * @param window The window to disable system windows on
     */
    @JvmStatic
    fun disableWindowSystemWindows(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    /**
     * Set the view to be edge to edge with system bars
     * @param view The view to set the insets on
     */
    @JvmStatic
    fun setViewEdgeToEdge(view: android.view.View) {
        setViewEdgeToEdge(view, WindowInsetsCompat.Type.systemBars())
    }

    /**
     * Set the view to be edge to edge with a specific inset type
     * @param view The view to set the insets on
     * @param insetType The type of insets to apply, e.g. [WindowInsetsCompat.Type.systemBars]
     */
    @JvmStatic
    fun setViewEdgeToEdge(view: android.view.View, @WindowInsetsCompat.Type.InsetsType insetType: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val insets = windowInsets.getInsets(insetType)
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }
    }
}