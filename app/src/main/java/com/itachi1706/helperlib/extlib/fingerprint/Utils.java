package com.itachi1706.helperlib.extlib.fingerprint;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.AttrRes;

/**
 * @deprecated Slated for removal soon
 */
@Deprecated
class Utils {

    public static int resolveColor(Context context, @AttrRes int attr) {
        return resolveColor(context, attr, 0);
    }

    public static int resolveColor(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, fallback);
        } finally {
            a.recycle();
        }
    }
}