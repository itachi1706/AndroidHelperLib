package com.itachi1706.helperlib.interfaces

/**
 * Created by Kenneth on 15/1/2020.
 * for com.itachi1706.helperlib.interfaces in Helper Library
 */
interface LogHandler {
    fun handleExtraLogging(logLevel: Int, tag: String, message: String)
}