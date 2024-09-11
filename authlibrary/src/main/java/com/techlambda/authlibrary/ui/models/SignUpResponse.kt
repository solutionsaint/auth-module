package com.techlambda.authlibrary.ui.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
data class SignUpResponse(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("isDeleted") val isDeleted: Boolean,
    @SerializedName("referenceId") val referenceId: String,
    // Not used now
    // @SerializedName("instituteList") val instituteList: List<S>,
    @SerializedName("_id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int
)
