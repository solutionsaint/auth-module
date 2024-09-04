package com.techlambda.authlibrary.ui.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CodeVerificationResponse(
    @SerializedName("exists") val exists: Boolean,
    @SerializedName("users") val users: SignUpResponse
)