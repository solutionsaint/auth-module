package com.techlambda.authlibrary.ui.models

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("isDeleted") val isDeleted: Boolean,
    @SerializedName("instituteList") val instituteList: List<Any>,
    @SerializedName("_id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int
)