package com.techlambda.authlibrary.ui.data

import com.techlambda.authlibrary.ui.models.SignUpResponse
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: String,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val isAdmin: Boolean,
    val uniqueId: String?
)

fun SignUpResponse.toUserData(): UserData {
    return UserData(
        userId = userId,
        name = name,
        username = username,
        email = email,
        phone = phone,
        isAdmin = userType.contains("admin", ignoreCase = true),
        uniqueId = referenceId
    )
}