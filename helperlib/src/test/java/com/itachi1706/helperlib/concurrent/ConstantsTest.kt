package com.itachi1706.helperlib.concurrent

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstantsTest {

    @Test
    fun statusPending() {
        assertEquals(Constants.Status.PENDING, Constants.Status.valueOf("PENDING"))
    }

    @Test
    fun statusRunning() {
        assertEquals(Constants.Status.RUNNING, Constants.Status.valueOf("RUNNING"))
    }

    @Test
    fun statusFinished() {
        assertEquals(Constants.Status.FINISHED, Constants.Status.valueOf("FINISHED"))
    }

    @Test
    fun statusInvalid() {
        try {
            Constants.Status.valueOf("INVALID")
        } catch (e: IllegalArgumentException) {
            assertEquals("No enum constant com.itachi1706.helperlib.concurrent.Constants.Status.INVALID", e.message)
        }
    }
}