package com.techlambda.authlibrary.ui.models

data class SignUpRequest(
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val userType: String,
    val fcmToken: String,
    val appId: String
)

data class SignInRequest(
    val email: String,
    val password: String,
    val type: String = "email",
    val fcmToken: String,
    val appId: String
)

data class OtpRequest(
    val email: String?=null,
    val otp: String? = null
)

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String? = null
)

data class CodeVerificationRequest(
    val userId: String,
    val code: String
)

data class ApiResponse<T>(
    val message: String? = null,
    val data: T? = null,
    val statusCode: Int? = null
)

data class VerifyUser(
    val emailId: String
)

data class UpdateProfileRequest(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val id: String
)

data class FilterRequest(
    val filter: FilterData
)

data class FilterData(
    val value: ValueData
)

data class ValueData(
    val field: String,
    val op: String,
    val value: String
)