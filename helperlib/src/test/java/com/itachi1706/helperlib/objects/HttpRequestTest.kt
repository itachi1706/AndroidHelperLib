package com.itachi1706.helperlib.objects

import com.android.volley.Request
import kotlin.test.Test
import kotlin.test.assertEquals

class HttpRequestTest {

    @Test
    fun mapToVolleyMethodsReturnsGetForGet() {
        assertEquals(Request.Method.GET, HttpRequest.mapToVolleyMethods(HttpRequest.Method.GET))
    }

    @Test
    fun mapToVolleyMethodsReturnsPostForPost() {
        assertEquals(Request.Method.POST, HttpRequest.mapToVolleyMethods(HttpRequest.Method.POST))
    }

    @Test
    fun mapToVolleyMethodsReturnsPutForPut() {
        assertEquals(Request.Method.PUT, HttpRequest.mapToVolleyMethods(HttpRequest.Method.PUT))
    }

    @Test
    fun mapToVolleyMethodsReturnsDeleteForDelete() {
        assertEquals(Request.Method.DELETE, HttpRequest.mapToVolleyMethods(HttpRequest.Method.DELETE))
    }

    @Test
    fun mapToVolleyMethodsReturnsPatchForPatch() {
        assertEquals(Request.Method.PATCH, HttpRequest.mapToVolleyMethods(HttpRequest.Method.PATCH))
    }

    @Test
    fun mapToVolleyMethodsReturnsHeadForHead() {
        assertEquals(Request.Method.HEAD, HttpRequest.mapToVolleyMethods(HttpRequest.Method.HEAD))
    }

    @Test
    fun mapToVolleyMethodsReturnsOptionsForOptions() {
        assertEquals(Request.Method.OPTIONS, HttpRequest.mapToVolleyMethods(HttpRequest.Method.OPTIONS))
    }

    @Test
    fun mapToVolleyMethodsReturnsTraceForTrace() {
        assertEquals(Request.Method.TRACE, HttpRequest.mapToVolleyMethods(HttpRequest.Method.TRACE))
    }
}

