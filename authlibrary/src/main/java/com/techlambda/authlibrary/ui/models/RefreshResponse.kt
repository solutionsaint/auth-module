package com.techlambda.authlibrary.ui.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("data")
    @Expose
    val accessToken: String
)