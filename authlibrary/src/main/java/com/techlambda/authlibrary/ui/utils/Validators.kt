package com.techlambda.authlibrary.ui.utils

import java.util.regex.Pattern

fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return Pattern.matches(emailPattern, email)
}

fun isValidPhoneNumber(phoneNumber: String): Boolean {
    val phoneNumberPattern = "^[+]?[0-9]{10,13}\$"
    return Pattern.matches(phoneNumberPattern, phoneNumber)
}

fun isPhoneNumber(input: String): Boolean {
    val numericPattern = "^[0-9]+$"
    return input.matches(Regex(numericPattern))
}