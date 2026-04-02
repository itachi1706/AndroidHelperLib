package com.itachi1706.helperlib.exceptions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApiExceptionTest {

    @Test
    fun apiExceptionPreservesMessage() {
        val exception = ApiException("Callback is not set")

        assertEquals("Callback is not set", exception.message)
    }

    @Test
    fun apiExceptionExtendsExceptionType() {
        val exception = ApiException("Any message")
        val asException: Exception = exception

        assertEquals("Any message", asException.message)
    }

    @Test
    fun apiExceptionHasNullCauseByDefault() {
        val exception = ApiException("Any message")

        assertNull(exception.cause)
    }
}


