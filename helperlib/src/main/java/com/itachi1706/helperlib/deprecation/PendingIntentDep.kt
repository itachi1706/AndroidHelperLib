@file:Suppress("unused")

package com.itachi1706.helperlib.deprecation

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

@JvmOverloads
fun Context.getImmutableActivity(code: Int, intent: Intent, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(this, code, intent, flags or PendingIntent.FLAG_IMMUTABLE, options)
    } else {
        PendingIntent.getActivity(this, code, intent, flags, options)
    }
}

@JvmOverloads
fun Context.getMutableActivity(code: Int, intent: Intent, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(this, code, intent, flags or PendingIntent.FLAG_MUTABLE, options)
    } else {
        PendingIntent.getActivity(this, code, intent, flags, options)
    }
}

@JvmOverloads
fun Context.getImmutableActivities(code: Int, intents: Array<Intent>, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivities(this, code, intents, flags or PendingIntent.FLAG_IMMUTABLE, options)
    } else {
        PendingIntent.getActivities(this, code, intents, flags, options)
    }
}

@JvmOverloads
fun Context.getMutableActivities(code: Int, intents: Array<Intent>, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivities(this, code, intents, flags or PendingIntent.FLAG_MUTABLE, options)
    } else {
        PendingIntent.getActivities(this, code, intents, flags, options)
    }
}

@JvmOverloads
fun Context.getImmutableBroadcast(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getBroadcast(this, code, intent, flags or PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getBroadcast(this, code, intent, flags)
    }
}

@JvmOverloads
fun Context.getMutableBroadcast(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getBroadcast(this, code, intent, flags or PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getBroadcast(this, code, intent, flags)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@JvmOverloads
fun Context.getImmutableForegroundService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getForegroundService(this, code, intent, flags or PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getForegroundService(this, code, intent, flags)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@JvmOverloads
fun Context.getMutableForegroundService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getForegroundService(this, code, intent, flags or PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getForegroundService(this, code, intent, flags)
    }
}

@JvmOverloads
fun Context.getImmutableService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getService(this, code, intent, flags or PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getService(this, code, intent, flags)
    }
}

@JvmOverloads
fun Context.getMutableService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getService(this, code, intent, flags or PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getService(this, code, intent, flags)
    }
}