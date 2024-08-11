package com.techlambda.onlineeducation.model.Request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class LoginRequestModel(
    val email: String,
    val username: String?,
    val password: String,
    val role: String?
)
