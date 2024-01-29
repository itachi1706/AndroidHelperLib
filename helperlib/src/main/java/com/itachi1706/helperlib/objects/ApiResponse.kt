package com.itachi1706.helperlib.objects

import java.io.Serializable
import java.util.Date

data class ApiResponse(
    val date: Date,
    val status: Int,
    val message: String,
    val success: Boolean,
    val data: Any? = null,
    val error: String? = null
) : Serializable
