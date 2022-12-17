package com.itachi1706.helperlib.helpers

import android.util.Log
import androidx.annotation.WorkerThread
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLException

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.helpers in Helper Library
 *
 * Standard class for helping out with HttpURLConnection or HttpsURLConnections
 *
 * @property url URL? URL Object
 * @property mode Int
 * @property fallbackHttp Boolean
 * @property timeout Int
 * @constructor Construct URLHelper with a URL object
 */
@Suppress("unused")
class URLHelper(url: URL) {

    companion object {
        const val HTTP_CONN = 0
        const val HTTPS_CONN = 1
        const val HTTP_QUERY_TIMEOUT = 15000 //15 seconds timeout
    }

    private var url: URL? = url
    private var mode = 0
    private var fallbackHttp = true
    private var timeout = -1

    init {
        mode = if (url.protocol.equals("https", ignoreCase = true)) HTTPS_CONN else HTTP_CONN
    }

    /**
     * @param url String? String of URL to parse
     * @constructor Construct URLHelper with a String URL
     */
    constructor(url: String?) : this(URL(url))

    /**
     * Enable this if you wish to fallback to HTTP if any HTTPS connection fails
     *
     * This is enabled by default
     * @param fallbackHttp true to fallback, false otherwise
     * @return object instance
     */
    fun setFallbackToHttp(fallbackHttp: Boolean): URLHelper {
        this.fallbackHttp = fallbackHttp
        return this
    }

    /**
     * Sets a custom timeout value for the URL Connection
     *
     * Defaults to [HTTP_QUERY_TIMEOUT]
     * @param timeout Custom timeout value in int
     * @return object instance
     */
    fun setTimeoutValues(timeout: Int): URLHelper {
        this.timeout = timeout
        return this
    }

    /**
     * Executes the HTTP request and obtain a String from it
     *
     * This can only be accomplished on a worker thread as it is blocking
     * @return String of the request
     * @throws IOException If an error occurrs
     */
    @WorkerThread
    @Throws(IOException::class)
    fun executeString(): String? { return if (mode == HTTPS_CONN) processHttpsConnection() else processHttpConnection() }

    @WorkerThread
    @Throws(IOException::class)
    private fun processHttpConnection(): String? {
        val conn = url!!.openConnection() as HttpURLConnection
        conn.connectTimeout = if (timeout == -1) HTTP_QUERY_TIMEOUT else timeout
        conn.readTimeout = if (timeout == -1) HTTP_QUERY_TIMEOUT else timeout
        val `in` = conn.inputStream
        val reader = BufferedReader(InputStreamReader(`in`))
        val str = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            str.append(line)
        }
        `in`.close()
        return str.toString()
    }

    @WorkerThread
    @Throws(IOException::class)
    private fun processHttpsConnection(): String? {
        try {
            val conn = url!!.openConnection() as HttpsURLConnection
            conn.connectTimeout = if (timeout == -1) HTTP_QUERY_TIMEOUT else timeout
            conn.readTimeout = if (timeout == -1) HTTP_QUERY_TIMEOUT else timeout
            conn.connect()
            // We will do a check for HTTPS error if there is a fallback support enabled
            if (fallbackHttp) if (conn.responseCode >= 300) return doFallback() // Fallback to HTTP
            val `in` = conn.inputStream
            val reader = BufferedReader(InputStreamReader(`in`))
            val str = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                str.append(line)
            }
            `in`.close()
            return str.toString()
        } catch (ignored: SSLException) {
            Log.d("HttpsUrlConn", "SSLException has occurred!")
            if (fallbackHttp) return doFallback()
        }
        return ""
    }

    @Throws(IOException::class)
    private fun doFallback(): String? {
        Log.i("HttpsUrlConn", "Error detected in HTTPS Connection. Doing fallback to HTTP")
        url = URL("http", url!!.host, url!!.port, url!!.file)
        return processHttpConnection()
    }
}