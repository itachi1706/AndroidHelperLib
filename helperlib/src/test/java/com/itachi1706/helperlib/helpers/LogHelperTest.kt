package com.itachi1706.helperlib.helpers

import com.itachi1706.helperlib.interfaces.LogHandler
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class LogHelperTest {

    @Test
    fun getLogLevelCharReturnsErrorForError() {
        assertEquals('E', LogHelper.getLogLevelChar(6))
    }

    @Test
    fun getLogLevelCharReturnsWarnForWarn() {
        assertEquals('W', LogHelper.getLogLevelChar(5))
    }

    @Test
    fun getLogLevelCharReturnsDebugForDebug() {
        assertEquals('D', LogHelper.getLogLevelChar(3))
    }

    @Test
    fun getLogLevelCharReturnsInfoForInfo() {
        assertEquals('I', LogHelper.getLogLevelChar(4))
    }

    @Test
    fun getLogLevelCharReturnsVerboseForUnknownLevel() {
        assertEquals('V', LogHelper.getLogLevelChar(Int.MAX_VALUE))
    }

    @Test
    fun getGenericLogStringFormatsLevelTagAndMessage() {
        val log = LogHelper.getGenericLogString(5, "Network", "Timeout")

        assertEquals("W/Network: Timeout", log)
    }

    @Test
    fun addExternalLogForwardsDebugMessagesToExternalLogger() {
        val logger = mock<LogHandler>()
        LogHelper.addExternalLog(logger)

        LogHelper.d("UnitTest", "hello")

        verify(logger).handleExtraLogging(3, "UnitTest", "hello")
    }
}


