package com.itachi1706.helperlib.deprecation

import android.os.Build
import android.text.Spanned

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.deprecation in Helper Library
 */
@Suppress("DEPRECATION", "unused")
object HtmlDep {
    @JvmStatic
    fun fromHtml(source: String): Spanned {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) android.text.Html.fromHtml(source)
        else android.text.Html.fromHtml(source, android.text.Html.FROM_HTML_MODE_LEGACY)
    }
}