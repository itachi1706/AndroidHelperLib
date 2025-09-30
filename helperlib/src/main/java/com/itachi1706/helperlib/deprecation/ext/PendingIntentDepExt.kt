@file:Suppress("unused")

package com.itachi1706.helperlib.deprecation.ext

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.itachi1706.helperlib.deprecation.PendingIntentDep

@JvmOverloads
fun Context.getImmutableActivity(code: Int, intent: Intent, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return PendingIntentDep.getImmutableActivity(this, code, intent, flags, options)
}

@JvmOverloads
fun Context.getMutableActivity(code: Int, intent: Intent, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return PendingIntentDep.getMutableActivity(this, code, intent, flags, options)
}

@JvmOverloads
fun Context.getImmutableActivities(code: Int, intents: Array<Intent>, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return PendingIntentDep.getImmutableActivities(this, code, intents, flags, options)
}

@JvmOverloads
fun Context.getMutableActivities(code: Int, intents: Array<Intent>, flags: Int = 0, options: Bundle? = null): PendingIntent {
    return PendingIntentDep.getMutableActivities(this, code, intents, flags, options)
}

@JvmOverloads
fun Context.getImmutableBroadcast(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return PendingIntentDep.getImmutableBroadcast(this, code, intent, flags)
}

@JvmOverloads
fun Context.getMutableBroadcast(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return PendingIntentDep.getMutableBroadcast(this, code, intent, flags)
}

@RequiresApi(Build.VERSION_CODES.O)
@JvmOverloads
fun Context.getImmutableForegroundService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return PendingIntentDep.getImmutableForegroundService(this, code, intent, flags)
}

@RequiresApi(Build.VERSION_CODES.O)
@JvmOverloads
fun Context.getMutableForegroundService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return PendingIntentDep.getMutableForegroundService(this, code, intent, flags)
}

@JvmOverloads
fun Context.getImmutableService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return PendingIntentDep.getImmutableService(this, code, intent, flags)
}

@JvmOverloads
fun Context.getMutableService(code: Int, intent: Intent, flags: Int = 0): PendingIntent {
    return PendingIntentDep.getMutableService(this, code, intent, flags)
}