package com.itachi1706.helperlib.helpers

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.itachi1706.helperlib.objects.ApiResponse
import kotlinx.serialization.json.Json

@Suppress("unused")
class ApiCallsHelper(context: Context, private val baseUrl: String = DEFAULT_URL, private val tag: String = "DEFAULT") {

    // Set up volley request queue
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun cancelAllRequests() {
        queue.cancelAll(tag)
    }

    fun getSequenceNumber(): Int {
        return queue.sequenceNumber
    }

    fun makeGetCall(path: String, listener: ApiCallListener) {
        // Make a HTTP GET Call to the URL and path with Volley
        val url = "$baseUrl/$path"

        val request = StringRequest(Request.Method.GET, url, { response ->
            listener.onApiCallSuccess(serializeResponse(response))
        }, { error ->
            listener.onApiCallError(error.message ?: UNKNOWN_ERROR)
        })
        request.tag = tag
        queue.add(request)
    }

    fun makePostCall(path: String, data: String, listener: ApiCallListener) {
        // Make a HTTP POST Call to the URL and path
        val url = "$baseUrl/$path"

        val request = object : StringRequest(Method.POST, url, { response ->
            listener.onApiCallSuccess(serializeResponse(response))
        }, { error ->
            listener.onApiCallError(error.message ?: UNKNOWN_ERROR)
        }) {
            override fun getBody(): ByteArray {
                return data.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        request.tag = tag
        queue.add(request)
    }

    fun makePutCall(path: String, data: String, listener: ApiCallListener) {
        // Make a HTTP PUT Call to the URL and path
        val url = "$baseUrl/$path"

        val request = object : StringRequest(Method.PUT, url, { response ->
            listener.onApiCallSuccess(serializeResponse(response))
        }, { error ->
            listener.onApiCallError(error.message ?: UNKNOWN_ERROR)
        }) {
            override fun getBody(): ByteArray {
                return data.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        request.tag = tag
        queue.add(request)
    }

    fun makeDeleteCall(path: String, listener: ApiCallListener) {
        // Make a HTTP DELETE Call to the URL and path
        val url = "$baseUrl/$path"

        val request = StringRequest(Request.Method.DELETE, url, { response ->
            listener.onApiCallSuccess(serializeResponse(response))
        }, { error ->
            listener.onApiCallError(error.message ?: UNKNOWN_ERROR)
        })
        request.tag = tag
        queue.add(request)
    }

    private fun serializeResponse(response: String): ApiResponse {
        return Json.decodeFromString<ApiResponse>(response)
    }

    interface ApiCallListener {
        fun onApiCallSuccess(response: ApiResponse)
        fun onApiCallError(error: String)
    }

    companion object {
        const val LOG_TAG = "ApiCallsHelper"
        const val UNKNOWN_ERROR = "Unknown Error"
        const val DEFAULT_URL = "https://api.itachi1706.com"
    }
}