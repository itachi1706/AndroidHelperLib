package com.itachi1706.helperlib.helpers

import android.util.Log
import com.itachi1706.helperlib.interfaces.LogHandler

/**
 * Created by Kenneth on 15/1/2020.
 * for com.itachi1706.helperlib.helpers in Helper Library
 */
@Suppress("unused")
object LogHelper {

    private var externalLog: LogHandler? = null

    @JvmStatic
    fun addExternalLog(externalLogger: LogHandler) {
        externalLog = externalLogger
    }

    private fun externalLogging(logLevel: Int, tag: String, message: String) {
        externalLog?.handleExtraLogging(logLevel, tag, message)
    }

    private fun log(logLevel: Int, tag: String, message: String) {
        externalLogging(logLevel, tag, message)
        Log.println(logLevel, tag, message)
    }

    @JvmStatic
    fun d(tag: String, message: String) {
        log(Log.DEBUG, tag, message)
    }

    @JvmStatic
    fun d(tag: String, message: String, tr: Throwable) {
        externalLogging(Log.DEBUG, tag, message)
        externalLogging(Log.DEBUG, tag, EXCEPTION_THROWN_MSG + tr.message)
        Log.d(tag, message, tr)
    }

    @JvmStatic
    fun i(tag: String, message: String) {
        log(Log.INFO, tag, message)
    }

    @JvmStatic
    fun v(tag: String, message: String) {
        log(Log.VERBOSE, tag, message)
    }

    @JvmStatic
    fun e(tag: String, message: String) {
        log(Log.ERROR, tag, message)
    }

    @JvmStatic
    fun e(tag: String, message: String, tr: Throwable) {
        externalLogging(Log.ERROR, tag, message)
        externalLogging(Log.ERROR, tag, EXCEPTION_THROWN_MSG + tr.message)
        Log.e(tag, message, tr)
    }

    @JvmStatic
    fun w(tag: String, tr: Throwable) {
        externalLogging(Log.WARN, tag, EXCEPTION_THROWN_MSG + tr.message)
        Log.w(tag, tr)
    }

    @JvmStatic
    fun w(tag: String, message: String, tr: Throwable) {
        externalLogging(Log.WARN, tag, message)
        externalLogging(Log.WARN, tag, EXCEPTION_THROWN_MSG + tr.message)
        Log.w(tag, message, tr)
    }

    @JvmStatic
    fun w(tag: String, message: String) {
        log(Log.WARN, tag, message)
    }

    @JvmStatic
    fun wtf(tag: String, message: String) {
        Log.wtf(tag, message)
    }

    @JvmStatic
    fun getLogLevelChar(logLevel: Int): Char {
        return when (logLevel) {
            Log.ERROR -> 'E'
            Log.WARN -> 'W'
            Log.DEBUG -> 'D'
            Log.INFO -> 'I'
            else -> 'V'
        }
    }

    @JvmStatic
    fun getGenericLogString(logLevel: Int, tag: String, message: String): String {
        return "${getLogLevelChar(logLevel)}/$tag: $message"
    }

    private const val EXCEPTION_THROWN_MSG = "Exception thrown: "
}