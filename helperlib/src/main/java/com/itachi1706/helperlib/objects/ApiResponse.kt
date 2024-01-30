package com.itachi1706.helperlib.objects

import java.io.Serializable
import java.util.Date

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
data class ApiResponse(
    val date: Date,
    val status: Int,
    val message: String,
    val success: Boolean,
    val data: Any? = null,
    val error: String? = null
) : Serializable
