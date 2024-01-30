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
/**
 * API Call Handling class
 * @param context Context
 * @param baseUrl Base URL to use (Default: https://api.itachi1706.com)
 * @param tag Tag to use for Volley Request Queue (Default: DEFAULT)
 */
class ApiCallsHelper(
    context: Context,
    private val baseUrl: String = DEFAULT_URL,
    private val tag: String = "DEFAULT"
) {

    /**
     * Volley Request Queue
     */
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    /**
     * Cancels all requests with the tag
     */
    fun cancelAllRequests() {
        queue.cancelAll(tag)
    }

    /**
     * Gets the current sequence number of the request queue
     * @return Sequence Number
     */
    fun getSequenceNumber(): Int {
        return queue.sequenceNumber
    }

    /**
     * Makes a GET Call to the specified path
     * @param path Path to call
     * @param listener Listener to handle the response on
     * @see ApiCallListener
     * @see ApiResponse
     * @see StringRequest
     * @see internalCallHandling
     */
    fun makeGetCall(path: String, listener: ApiCallListener) {
        internalCallHandling(Request.Method.GET, path, null, listener)
    }

    /**
     * Makes a POST Call to the specified path
     * @param path Path to call
     * @param data Data to send
     * @param listener Listener to handle the response on
     * @see ApiCallListener
     * @see ApiResponse
     * @see StringRequest
     * @see internalCallHandling
     */
    fun makePostCall(path: String, data: String?, listener: ApiCallListener) {
        internalCallHandling(Request.Method.POST, path, data, listener)
    }

    /**
     * Makes a PUT Call to the specified path
     * @param path Path to call
     * @param data Data to send
     * @param listener Listener to handle the response on
     * @see ApiCallListener
     * @see ApiResponse
     * @see StringRequest
     * @see internalCallHandling
     */
    fun makePutCall(path: String, data: String?, listener: ApiCallListener) {
        internalCallHandling(Request.Method.PUT, path, data, listener)
    }

    /**
     * Makes a DELETE Call to the specified path
     * @param path Path to call
     * @param listener Listener to handle the response on
     * @see ApiCallListener
     * @see ApiResponse
     * @see StringRequest
     * @see internalCallHandling
     */
    fun makeDeleteCall(path: String, listener: ApiCallListener) {
        internalCallHandling(Request.Method.DELETE, path, null, listener)
    }

    /**
     * Makes a HTTP Call to the specified path
     * @param method HTTP Method to use
     * @param path Path to call
     * @param data Data to send if any
     * @param listener Listener to handle the response on
     * @see ApiCallListener
     * @see ApiResponse
     * @see StringRequest
     * @see internalCallHandling
     */
    private fun internalCallHandling(
        method: Int,
        path: String,
        data: String?,
        listener: ApiCallListener
    ) {
        // Make a HTTP Call to the URL and path
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

    /**
     * Serializes the response into an ApiResponse object
     * @param response Response to serialize
     * @return ApiResponse object
     */
    private fun serializeResponse(response: String): ApiResponse {
        return Json.decodeFromString<ApiResponse>(response)
    }

    /**
     * Interface to handle API Calls
     */
    interface ApiCallListener {
        /**
         * Called when API Call is successful
         * @param response ApiResponse object
         */
        fun onApiCallSuccess(response: ApiResponse)

        /**
         * Called when API Call fails
         * @param error Error message
         */
        fun onApiCallError(error: String)
    }

    companion object {
        private const val LOG_TAG = "ApiCallsHelper"
        private const val UNKNOWN_ERROR = "Unknown Error"
        private const val DEFAULT_URL = "https://api.itachi1706.com"
    }
}