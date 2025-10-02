package com.itachi1706.helperlib.objects

import com.android.volley.Request

object HttpRequest {
    enum class Method {
        GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE
    }

    fun mapToVolleyMethods(method: Method): Int {
        return when (method) {
            Method.GET -> Request.Method.GET
            Method.POST -> Request.Method.POST
            Method.PUT -> Request.Method.PUT
            Method.DELETE -> Request.Method.DELETE
            Method.PATCH -> Request.Method.PATCH
            Method.HEAD -> Request.Method.HEAD
            Method.OPTIONS -> Request.Method.OPTIONS
            Method.TRACE -> Request.Method.TRACE
        }
    }
}