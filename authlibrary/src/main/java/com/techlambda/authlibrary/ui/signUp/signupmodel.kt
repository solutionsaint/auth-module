package com.techlambda.authlibrary.ui.signUp

data class SignUpRequest(
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val userType: String
)

data class SignInRequest(
    val email: String,
    val password: String,
    val type: String = "email"
)

data class OtpRequest(
    val email: String?=null,
    val otp: String? = null
)

data class ResetPasswordRequest(
    val email: String,
    val type: String,
    val username: String? = null,
    val otp: String? = null,
    val password: String? = null
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val statusCode: Int
)
