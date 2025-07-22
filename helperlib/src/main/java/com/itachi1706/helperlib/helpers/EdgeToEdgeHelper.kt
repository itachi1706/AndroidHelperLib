package com.itachi1706.helperlib.helpers

import android.view.View
import android.view.Window
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("unused")
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
    fun setViewEdgeToEdge(view: View) {
        setViewEdgeToEdge(view, WindowInsetsCompat.Type.systemBars())
    }

    /**
     * Set the view to be edge to edge with a specific inset type
     * @param view The view to set the insets on
     * @param insetType The type of insets to apply, e.g. [WindowInsetsCompat.Type.systemBars]
     */
    @JvmStatic
    fun setViewEdgeToEdge(
        view: View,
        @WindowInsetsCompat.Type.InsetsType insetType: Int
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            // Get the insets for the system bars (status bar, navigation bar) and apply padding
            val insets = windowInsets.getInsets(insetType)
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED // Indicate that we have consumed the insets
        }
    }

    /**
     * Set the activity to be edge to edge with system bars and set the content view
     * @param view The view to set the insets on
     * @param activity The activity to set the content view on
     * @param layoutId The layout resource ID to set as content view
     */
    @JvmStatic
    fun setEdgeToEdgeWithContentView(
        view: View,
        activity: AppCompatActivity,
        @LayoutRes layoutId: Int
    ) {
        disableWindowSystemWindows(activity.window)
        activity.setContentView(layoutId)
        setViewEdgeToEdge(view)
    }

    /**
     * Set the activity to be edge to edge with system bars and set the content view
     * @param view The view to set the insets on
     * @param activity The activity to set the content view on
     * @param layoutView The view to set as content view
     */
    @JvmStatic
    fun setEdgeToEdgeWithContentView(
        view: View,
        activity: AppCompatActivity,
        layoutView: View
    ) {
        disableWindowSystemWindows(activity.window)
        activity.setContentView(layoutView)
        setViewEdgeToEdge(view)
    }

    /**
     * Set the activity to be edge to edge with system bars and set the content view
     * @param viewId The ID of the view to set the insets on
     * @param activity The activity to set the content view on
     * @param layoutView The view to set as content view
     */
    @JvmStatic
    fun setEdgeToEdgeWithContentView(
        @IdRes viewId: Int,
        activity: AppCompatActivity,
        layoutView: View
    ) {
        disableWindowSystemWindows(activity.window)
        activity.setContentView(layoutView)
        val view = activity.findViewById<View>(viewId)
        setViewEdgeToEdge(view)
    }

    /**
     * Set the activity to be edge to edge with system bars and set the content view
     * @param viewId The ID of the view to set the insets on
     * @param activity The activity to set the content view on
     * @param layoutId The layout resource ID to set as content view
     */
    @JvmStatic
    fun setEdgeToEdgeWithContentView(
        @IdRes viewId: Int,
        activity: AppCompatActivity,
        @LayoutRes layoutId: Int
    ) {
        disableWindowSystemWindows(activity.window)
        activity.setContentView(layoutId)
        val view = activity.findViewById<View>(viewId)
        setViewEdgeToEdge(view)
    }

    /**
     * Set the activity to be edge to edge with system bars and set the content view
     * @param layoutView The view to set the insets on
     * @param activity The activity to set the content view on
     */
    @JvmStatic
    fun setEdgeToEdgeWithContentView(
        layoutView: View,
        activity: AppCompatActivity
    ) {
        disableWindowSystemWindows(activity.window)
        activity.setContentView(layoutView)
        setViewEdgeToEdge(layoutView)
    }
}