package com.techlambda.onlineeducation.model.Response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    val token: String,
    val userId: String,
    val username: String
)
