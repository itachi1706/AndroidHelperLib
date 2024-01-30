package com.itachi1706.helperlib.helpers

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.itachi1706.helperlib.objects.ApiResponse
import kotlinx.serialization.json.Json

@Suppress("unused")
class ApiCallsHelper(
    context: Context,
    private val baseUrl: String = DEFAULT_URL,
    private val tag: String = "DEFAULT"
) {

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
        internalCallHandling(Request.Method.GET, path, null, listener)
    }

    fun makePostCall(path: String, data: String?, listener: ApiCallListener) {
        // Make a HTTP POST Call to the URL and path
        internalCallHandling(Request.Method.POST, path, data, listener)
    }

    fun makePutCall(path: String, data: String?, listener: ApiCallListener) {
        // Make a HTTP PUT Call to the URL and path
        internalCallHandling(Request.Method.PUT, path, data, listener)
    }

    fun makeDeleteCall(path: String, listener: ApiCallListener) {
        // Make a HTTP DELETE Call to the URL and path
        internalCallHandling(Request.Method.DELETE, path, null, listener)
    }

    private fun internalCallHandling(
        method: Int,
        path: String,
        data: String?,
        listener: ApiCallListener
    ) {
        // Make a HTTP DELETE Call to the URL and path
        val url = "$baseUrl/$path"

        val successListener = Response.Listener { response: String ->
            listener.onApiCallSuccess(serializeResponse(response))
        }
        val failedListener = Response.ErrorListener { error ->
            listener.onApiCallError(
                error?.message ?: UNKNOWN_ERROR
            )
        }

        val request: StringRequest = if (data != null) {
            object : StringRequest(method, url, successListener, failedListener) {
                override fun getBody(): ByteArray {
                    return data.toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        } else {
            StringRequest(method, url, successListener, failedListener)
        }

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