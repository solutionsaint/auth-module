package com.techlambda.onlineeducation.model.Request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestModel(
    val username: String,
    val password: String,
    val email: String

)
