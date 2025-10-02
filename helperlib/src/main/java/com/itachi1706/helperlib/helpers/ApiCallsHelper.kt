package com.itachi1706.helperlib.helpers

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.itachi1706.helperlib.exceptions.ApiException
import com.itachi1706.helperlib.objects.ApiResponse
import com.itachi1706.helperlib.objects.HttpRequest
import kotlinx.serialization.json.Json

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * API Call Handling class
 * @param context Context
 * @param baseUrl Base URL to use (Default: https://api.itachi1706.com)
 * @param tag Tag to use for Volley Request Queue (Default: DEFAULT)
 * @param extraHeaders Extra Headers to add to the request (Default: None)
 * @param defaultAuthentication Whether to add default authentication headers (Default: False)
 * @see ApiCallListener
 * @see ApiResponse
 */
class ApiCallsHelper(
    private val context: Context,
    private val baseUrl: String = DEFAULT_URL,
    private val tag: String = "DEFAULT",
    private val extraHeaders: MutableMap<String, String> = mutableMapOf(),
    private val defaultAuthentication: Boolean = false
) {

    /**
     * Builder class for ApiCallsHelper
     *
     * This is used to create a Java-styled Builder Pattern ([build]) or if you wish to make single HTTP requests ([makeRequest])
     *
     * Note: For if calling [makeRequest] for Single HTTP requests, [callback] MUST be set via [setCallback] or an [ApiException] will be thrown
     *
     * @param context Context
     * @see ApiCallsHelper
     * @see ApiException
     * @throws ApiException If [callback] is not set. Ensure that [setCallback] is called before calling this method
     */
    class Builder(private val context: Context) {

        // Create variables
        private var baseUrl: String = DEFAULT_URL
        private var tag: String = "DEFAULT"
        private var extraHeaders: MutableMap<String, String> = mutableMapOf()
        private var defaultAuthentication: Boolean = false

        // If not just building
        private var callback: ApiCallListener? = null
        private var method: Int = Request.Method.GET
        private var data: String? = null

        private var path: String = ""

        /**
         * Set the base URL to use for API calls
         * @param baseUrl Base URL to use defaults to [DEFAULT_URL]
         */
        fun setBaseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        /**
         * Set the tag to use for Volley Request Queue
         * @param tag Tag to use defaults to "DEFAULT"
         */
        fun setTag(tag: String): Builder {
            this.tag = tag
            return this
        }

        /**
         * Set extra headers to add to the request
         * @param extraHeaders Extra Headers to add to the request
         */
        fun setExtraHeaders(extraHeaders: MutableMap<String, String>): Builder {
            this.extraHeaders = extraHeaders
            return this
        }

        /**
         * Add an extra header to the request. If the header already exists, it will be overwritten
         * @param key Key of the header
         * @param value Value of the header
         */
        fun addHeaders(key: String, value: String): Builder {
            this.extraHeaders[key] = value
            return this
        }

        /**
         * Set whether to add default authentication headers
         * @param defaultAuthentication Whether to add default authentication headers
         */
        fun setDefaultAuthentication(defaultAuthentication: Boolean): Builder {
            this.defaultAuthentication = defaultAuthentication
            return this
        }

        /**
         * Set the callback to use for API calls
         * @param callback Callback to use for API calls
         * @see ApiCallListener
         */
        fun setCallback(callback: ApiCallListener): Builder {
            this.callback = callback
            return this
        }

        /**
         * Set the method to use for API calls. Requires Android Volley dependency
         * @param method Method to use for API calls. See [Request.Method] for available methods
         * @see Request.Method
         */
        fun setMethod(method: Int): Builder {
            this.method = method
            return this
        }

        /**
         * Set the method to use for API calls
         * @param method Method to use for API calls. See [HttpRequest.Method] for available methods
         * @see HttpRequest.Method
         */
        fun setMethod(method: HttpRequest.Method): Builder {
            this.method = HttpRequest.mapToVolleyMethods(method)
            return this
        }

        /**
         * Set the data to use for API calls
         * @param data Data to use for API calls
         */
        fun setBody(data: String): Builder {
            this.data = data
            return this
        }

        /**
         * Set the path to use for API calls
         * @param path Path to use for API calls
         */
        fun setPath(path: String): Builder {
            this.path = path
            return this
        }

        /**
         * Builds the [ApiCallsHelper]
         * @return [ApiCallsHelper]
         */
        fun build(): ApiCallsHelper {
            return ApiCallsHelper(context, baseUrl, tag, extraHeaders, defaultAuthentication)
        }

        /**
         * Makes a request to the API. If [callback] is not set, an [ApiException] will be thrown
         *
         * Results will be returned via [callback]
         *
         * @throws ApiException If [callback] is not set. Ensure that [setCallback] is called before calling this method
         */
        fun makeRequest() {
            // Validate that listener is set
            if (callback == null) {
                throw ApiException("Callback is not set")
            }

            val helper = ApiCallsHelper(context, baseUrl, tag, extraHeaders, defaultAuthentication)
            helper.internalCallHandling(method, path, data, callback!!)
        }
    }

    /**
     * Gets the default authentication headers
     * @return Default authentication headers
     */
    private fun getDefaultAuthenticationHeaders(): Map<String, String> {
        val packageName = this.context.packageName
        val signature = ValidationHelper.getSignatureForValidation(this.context)

        if (defaultAuthentication) {
            return mapOf(
                "X-Package-Name" to packageName,
                "X-Signature" to signature
            )
        }
        return mapOf()
    }


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

        val request: StringRequest =
            object : StringRequest(method, url, successListener, failedListener) {
                override fun getBody(): ByteArray {
                    return data?.toByteArray() ?: super.getBody()
                }

                override fun getBodyContentType(): String {
                    return if (data == null) super.getBodyContentType()
                    else "application/json"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = mutableMapOf<String, String>()
                    headers.putAll(extraHeaders)
                    if (defaultAuthentication) {
                        headers.putAll(getDefaultAuthenticationHeaders())
                    }

                    return headers
                }
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
        private const val DEFAULT_URL = "https://api.itachi1706.com/v1"
    }
}