package com.techlambda.onlineeducation.model.Request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignupRequestModel(
    @SerializedName("name")
    @Expose
    val userName: String,
    @SerializedName("email")
    @Expose
    val email: String? = null,
    @SerializedName("phone")
    @Expose
    val mobileNumber: String? = null,
    @SerializedName("userType")
    @Expose
    val role: String = "patient",
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("deviceId")
    @Expose
    val deviceId: String,
)
