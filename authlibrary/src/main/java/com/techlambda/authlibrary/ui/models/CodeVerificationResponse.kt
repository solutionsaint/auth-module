package com.techlambda.authlibrary.ui.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CodeVerificationResponse(
    @field:SerializedName("statusCode")
    val statusCode: Int,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("result")
    val result: CodeResult
)

@Serializable
data class CodeResult(
    @field:SerializedName("acknowledged")
    val acknowledged: Boolean,
    @field:SerializedName("modifiedCount")
    val modifiedCount: Int,
    @field:SerializedName("upsertedId")
    val upsertedId: String,
    @field:SerializedName("upsertedCount")
    val upsertedCount: Int,
    @field:SerializedName("matchedCount")
    val matchedCount: Int
)