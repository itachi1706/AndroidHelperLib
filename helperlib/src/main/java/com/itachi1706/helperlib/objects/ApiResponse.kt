package com.itachi1706.helperlib.objects

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * API Response Object
 * @constructor Creates an API Response Object
 * @since 1.0.0
 * @property date Date of the response
 * @property status Status Code of the response
 * @property message Message of the response
 * @property success Success of the response
 * @property data Data of the response
 * @property error Error of the response
 * @see Serializable
 * @see Date
 */
@Suppress("unused")
@kotlinx.serialization.Serializable
data class ApiResponse(
    val date: String,
    val status: Int,
    val message: String,
    val success: Boolean,
    val data: JsonElement? = null,
    val error: String? = null
) : Serializable {
    inline fun <reified T> getTypedData(json: Json = Json): T? {
        return data?.let {
            json.decodeFromJsonElement<T>(it)
        }
    }

    fun getDateObject(): Date? {
        // Remove nanoseconds if present (keep up to milliseconds)
        val regex = Regex("""\.\d{3,9}""")
        val dateStr = regex.replace(date) { matchResult ->
            // Keep only first 3 digits (milliseconds)
            ".${matchResult.value.substring(1, 4)}"
        }
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            format.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }
}
