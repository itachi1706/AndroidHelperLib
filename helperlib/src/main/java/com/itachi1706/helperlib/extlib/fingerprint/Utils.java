package com.itachi1706.helperlib.extlib.fingerprint;

import android.content.Context;

import androidx.annotation.AttrRes;

import com.itachi1706.helperlib.utils.ColorUtil;

/**
 * @deprecated Slated for removal soon
 */
@Deprecated
class Utils {

    public static int resolveColor(Context context, @AttrRes int attr) {
        return ColorUtil.resolveColor(context, attr);
    }

    public static int resolveColor(Context context, @AttrRes int attr, int fallback) {
        return ColorUtil.resolveColor(context, attr, fallback);
    }
}