package com.techlambda.onlineeducation.model.Request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class LoginRequestModel (
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("type")
    @Expose
    val type: String
)
