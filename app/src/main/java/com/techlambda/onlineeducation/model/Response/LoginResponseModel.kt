package com.techlambda.onlineeducation.model.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class LoginResponseModel (
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