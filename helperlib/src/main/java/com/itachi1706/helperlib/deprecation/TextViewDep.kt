package com.itachi1706.helperlib.deprecation

import android.content.Context
import android.os.Build
import android.widget.TextView

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.deprecation in Helper Library
 */
@Suppress("DEPRECATION", "unused")
object TextViewDep {
    @JvmStatic
    fun setTextAppearance(textView: TextView, context: Context?, resId: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) textView.setTextAppearance(context, resId)
        else textView.setTextAppearance(resId)
    }
}