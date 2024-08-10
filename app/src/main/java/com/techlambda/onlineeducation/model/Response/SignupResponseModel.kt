package com.techlambda.onlineeducation.model.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SignupResponseModel(
    @SerializedName("statusCode")
    @Expose
    val status: Int,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("result")
    @Expose
    val result: SaveUser
)

data class SaveUser(
    @SerializedName("name")
    @Expose
    val name:String,
    @SerializedName("username")
    @Expose
    val username:String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String,
)