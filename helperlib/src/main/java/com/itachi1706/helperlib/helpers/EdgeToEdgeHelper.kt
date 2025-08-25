@file:Suppress("unused")

package com.itachi1706.helperlib.helpers

import android.view.View
import android.view.Window
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Disable the window system windows to allow edge to edge layout
 * @receiver The Application Window
 */
fun Window.disableWindowSystemWindows() {
    WindowCompat.setDecorFitsSystemWindows(this, false)
}

/**
 * Set the view to be edge to edge with system bars
 * @receiver The Application View
 */
fun View.setViewEdgeToEdge() {
    this.setViewEdgeToEdge(WindowInsetsCompat.Type.systemBars())
}

/**
 * Set the view to be edge to edge with a specific inset type
 * @receiver The Application View
 * @param insetType The type of insets to apply, e.g. [WindowInsetsCompat.Type.systemBars]
 */
fun View.setViewEdgeToEdge(@WindowInsetsCompat.Type.InsetsType insetType: Int) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
        // Get the insets for the system bars (status bar, navigation bar) and apply padding
        val insets = windowInsets.getInsets(insetType)
        v.setPadding(insets.left, insets.top, insets.right, insets.bottom)

        WindowInsetsCompat.CONSUMED // Indicate that we have consumed the insets
    }
}

/**
 * Set the activity to be edge to edge with system bars and set the content view
 * @receiver AppCompat Activity
 * @param view The view to set the insets on
 * @param layoutId The layout resource ID to set as content view
 */
fun AppCompatActivity.setEdgeToEdgeWithContentView(view: View, @LayoutRes layoutId: Int) {
    this.window.disableWindowSystemWindows()
    this.setContentView(layoutId)
    view.setViewEdgeToEdge()
}

/**
 * Set the activity to be edge to edge with system bars and set the content view
 * @receiver AppCompat Activity
 * @param view The view to set the insets on
 * @param layoutView The view to set as content view
 */
fun AppCompatActivity.setEdgeToEdgeWithContentView(view: View, layoutView: View) {
    this.window.disableWindowSystemWindows()
    this.setContentView(layoutView)
    view.setViewEdgeToEdge()
}

/**
 * Set the activity to be edge to edge with system bars and set the content view
 * @receiver AppCompat Activity
 * @param viewId The ID of the view to set the insets on
 * @param layoutView The view to set as content view
 */
fun AppCompatActivity.setEdgeToEdgeWithContentView(@IdRes viewId: Int, layoutView: View) {
    this.window.disableWindowSystemWindows()
    this.setContentView(layoutView)
    val view = this.findViewById<View>(viewId)
    view.setViewEdgeToEdge()
}

/**
 * Set the activity to be edge to edge with system bars and set the content view
 * @receiver AppCompat Activity
 * @param viewId The ID of the view to set the insets on
 * @param layoutId The layout resource ID to set as content view
 */
fun AppCompatActivity.setEdgeToEdgeWithContentView(@IdRes viewId: Int, @LayoutRes layoutId: Int) {
    this.window.disableWindowSystemWindows()
    this.setContentView(layoutId)
    val view = this.findViewById<View>(viewId)
    view.setViewEdgeToEdge()
}

/**
 * Set the activity to be edge to edge with system bars and set the content view
 * @receiver AppCompat Activity
 * @param layoutView The view to set the insets on
 */
fun AppCompatActivity.setEdgeToEdgeWithContentView(layoutView: View) {
    this.window.disableWindowSystemWindows()
    this.setContentView(layoutView)
    layoutView.setViewEdgeToEdge()
}