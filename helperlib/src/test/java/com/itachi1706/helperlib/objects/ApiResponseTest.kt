package com.itachi1706.helperlib.objects

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ApiResponseTest {

    @Test
    fun getTypedDataReturnsDecodedObjectWhenDataMatchesType() {
        val response = ApiResponse(
            date = "2025-10-01T10:29:38.201645045Z",
            status = 200,
            message = "OK",
            success = true,
            data = buildJsonObject {
                put("id", 7)
                put("name", "Kenneth")
            }
        )

        val typedData = response.getTypedData<TestPayload>()

        assertEquals(TestPayload(id = 7, name = "Kenneth"), typedData)
    }

    @Test
    fun getTypedDataReturnsNullWhenDataIsMissing() {
        val response = ApiResponse(
            date = "2025-10-01T10:29:38.201645045Z",
            status = 204,
            message = "No Content",
            success = true,
            data = null
        )

        assertNull(response.getTypedData<TestPayload>())
    }

    @Test
    fun getTypedDataUsesProvidedJsonConfigurationForUnknownKeys() {
        val response = ApiResponse(
            date = "2025-10-01T10:29:38.201645045Z",
            status = 200,
            message = "OK",
            success = true,
            data = buildJsonObject {
                put("id", 7)
                put("name", "Kenneth")
                put("ignored", "value")
            }
        )

        val typedData = response.getTypedData<TestPayload>(
            Json {
                ignoreUnknownKeys = true
            }
        )

        assertEquals(TestPayload(id = 7, name = "Kenneth"), typedData)
    }

    @Test
    fun getTypedDataUsesProvidedJsonConfigurationForLenientPayloads() {
        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }

        val response = json.decodeFromString<ApiResponse>(
            "{\"date\":\"2025-10-01T10:29:38.201645045Z\",\"status\":200,\"message\":\"OK\",\"success\":true,\"data\":{\"id\":7,\"name\":\"Kenneth\"},\"ignored\":\"value\"}"
        )

        val typedData = response.getTypedData<TestPayload>(json)

        assertEquals(TestPayload(id = 7, name = "Kenneth"), typedData)
    }

    @Test
    fun getTypedDataThrowsWhenPayloadDoesNotMatchRequestedType() {
        val response = ApiResponse(
            date = "2025-10-01T10:29:38.201645045Z",
            status = 200,
            message = "OK",
            success = true,
            data = JsonPrimitive("not a number")
        )

        assertFailsWith<IllegalArgumentException> {
            response.getTypedData<Int>()
        }
    }

    @Test
    fun getDateObjectParsesIsoDateWithNanosecondsByTruncatingToMilliseconds() {
        val response = ApiResponse(
            date = "2025-10-01T10:29:38.201645045Z",
            status = 200,
            message = "OK",
            success = true
        )

        val parsedDate = response.getDateObject()

        assertNotNull(parsedDate)
        assertEquals(1759314578201L, parsedDate.time)
    }

    @Test
    fun getDateObjectReturnsNullForInvalidDateString() {
        val response = ApiResponse(
            date = "not-a-date",
            status = 500,
            message = "Error",
            success = false
        )

        assertNull(response.getDateObject())
    }

    @Serializable
    private data class TestPayload(
        val id: Int,
        val name: String
    )
}



