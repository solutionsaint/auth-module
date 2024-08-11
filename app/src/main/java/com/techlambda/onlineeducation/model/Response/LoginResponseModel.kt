package com.techlambda.onlineeducation.model.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class LoginResponseModel(
    val Token: String,
)