package com.itachi1706.helperlib.helpers

import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationHelperTest {

    @Test
    fun bytesToHexFormatsSingleByteAsUppercaseHex() {
        val value = byteArrayOf(0x0A)

        assertEquals("0A", ValidationHelper.bytesToHex(value))
    }

    @Test
    fun bytesToHexFormatsMultipleBytesWithColonSeparator() {
        val value = byteArrayOf(0x12, 0x34, 0x56)

        assertEquals("12:34:56", ValidationHelper.bytesToHex(value))
    }

    @Test
    fun bytesToHexHandlesNegativeByteValuesCorrectly() {
        val value = byteArrayOf(0xFF.toByte(), 0x80.toByte(), 0x00)

        assertEquals("FF:80:00", ValidationHelper.bytesToHex(value))
    }

    @Test
    fun bytesToHexReturnsEmptyStringForEmptyArray() {
        assertEquals("", ValidationHelper.bytesToHex(byteArrayOf()))
    }
}

