package com.itachi1706.helperlib.temp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

@Deprecated("Use the non temp version. This is fallback for now")
@Suppress("UnspecifiedImmutableFlag", "Unused")
object PendingIntentDepTmp {

    @JvmStatic
    @JvmOverloads
    fun getImmutableActivity(activity: Context, code: Int, intent: Intent, flags: Int = 0, options: Bundle? = null): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(activity, code, intent, flags or PendingIntent.FLAG_IMMUTABLE, options)
        } else {
            PendingIntent.getActivity(activity, code, intent, flags, options)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getMutableActivity(activity: Context, code: Int, intent: Intent, flags: Int = 0, options: Bundle? = null): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(activity, code, intent, flags or PendingIntent.FLAG_MUTABLE, options)
        } else {
            PendingIntent.getActivity(activity, code, intent, flags, options)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getImmutableActivities(activity: Context, code: Int, intents: Array<Intent>, flags: Int = 0, options: Bundle? = null): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivities(activity, code, intents, flags or PendingIntent.FLAG_IMMUTABLE, options)
        } else {
            PendingIntent.getActivities(activity, code, intents, flags, options)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getMutableActivities(activity: Context, code: Int, intents: Array<Intent>, flags: Int = 0, options: Bundle? = null): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivities(activity, code, intents, flags or PendingIntent.FLAG_MUTABLE, options)
        } else {
            PendingIntent.getActivities(activity, code, intents, flags, options)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getImmutableBroadcast(activity: Context, code: Int, intent: Intent, flags: Int = 0): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(activity, code, intent, flags or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(activity, code, intent, flags)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getMutableBroadcast(activity: Context, code: Int, intent: Intent, flags: Int = 0): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(activity, code, intent, flags or PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(activity, code, intent, flags)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    @JvmOverloads
    fun getImmutableForegroundService(activity: Context, code: Int, intent: Intent, flags: Int = 0): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getForegroundService(activity, code, intent, flags or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getForegroundService(activity, code, intent, flags)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    @JvmOverloads
    fun getMutableForegroundService(activity: Context, code: Int, intent: Intent, flags: Int = 0): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getForegroundService(activity, code, intent, flags or PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getForegroundService(activity, code, intent, flags)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getImmutableService(activity: Context, code: Int, intent: Intent, flags: Int = 0): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getService(activity, code, intent, flags or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getService(activity, code, intent, flags)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getMutableService(activity: Context, code: Int, intent: Intent, flags: Int = 0): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getService(activity, code, intent, flags or PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getService(activity, code, intent, flags)
        }
    }
}