package com.itachi1706.helperlib.utils

import android.content.Context
import androidx.annotation.AttrRes

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.utils in Helper Library
 */
@Suppress("unused")
object ColorUtil {
    @JvmStatic
    fun resolveColor(context: Context, @AttrRes attr: Int): Int {
        return resolveColor(context, attr, 0)
    }

    @JvmStatic
    fun resolveColor(context: Context, @AttrRes attr: Int, fallback: Int): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        return try {
            a.getColor(0, fallback)
        } finally {
            a.recycle()
        }
    }
}