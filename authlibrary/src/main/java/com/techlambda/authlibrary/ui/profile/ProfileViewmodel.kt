package com.techlambda.authlibrary.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.data.AuthPrefManager
import com.techlambda.authlibrary.ui.data.UserData
import com.techlambda.authlibrary.ui.models.UpdateProfileRequest
import com.techlambda.authlibrary.ui.signUp.UserRepository
import com.techlambda.authlibrary.ui.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val prefManager: AuthPrefManager
) : ViewModel() {

    private val _uiStates = MutableStateFlow(ProfileUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<ProfileUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        _uiStates.update {
            _uiStates.value.copy(
                name = prefManager.getUserData()?.name ?: "",
                email = prefManager.getUserData()?.email ?: "",
                number = prefManager.getUserData()?.phone ?: "",
                id = prefManager.getUserData()?.id ?: ""
            )
        }
    }

    fun onEvent(event: ProfileUiActions) {
        when (event) {
            is ProfileUiActions.NameChanged -> {
                _uiStates.update {
                    it.copy(name = event.name)
                }
            }

            is ProfileUiActions.EmailChanged -> {
                _uiStates.value = _uiStates.value.copy(email = event.email)
            }

            is ProfileUiActions.NumberChanged -> {
                _uiStates.value = _uiStates.value.copy(number = event.number)
            }

            is ProfileUiActions.IdChanged -> {
                _uiStates.value = _uiStates.value.copy(id = event.id)
            }

            is ProfileUiActions.Update -> {
                updateProfile()
            }
        }
    }


    private fun updateProfile() {
        viewModelScope.launch {

            val req = UpdateProfileRequest(
                name = _uiStates.value.name,
                phone = _uiStates.value.number,
                email = _uiStates.value.email,
                id = _uiStates.value.id
            )
            Log.d("TAG", "updateProfile: $req")
            val response = repository.updateProfile(
                req
            )

            when (response) {
                is NetworkResult.Error -> {
                    _uiEvents.send(ProfileUiEvents.OnError("An error occurred during profile update: ${response.message}"))
                }

                is NetworkResult.Success -> {
                    val userObject = prefManager.getUserData()!!
                    prefManager.saveUserData(
                        UserData(
                            id = userObject.id,
                            name = response.data?.name ?: userObject.name,
                            email = response.data?.email ?: userObject.email,
                            uniqueId = userObject.uniqueId,
                            phone = response.data?.phone ?: userObject.phone,
                            userId = userObject.userId,
                            isAdmin = userObject.isAdmin,
                            username = userObject.username
                        )
                    )
                    _uiEvents.send(ProfileUiEvents.UpdateSuccess)
                }
            }
        }
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberPattern = "^[+]?[0-9]{10,13}\$"
        return Pattern.matches(phoneNumberPattern, phoneNumber)
    }

    fun isPhoneNumber(input: String): Boolean {
        val numericPattern = "^[0-9]+$"
        return input.matches(Regex(numericPattern))
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return Pattern.matches(emailPattern, email)
    }


}

data class ProfileUiState(
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val id: String = "",
    val isLoading: Boolean = false
)

sealed class ProfileUiActions {
    data class NameChanged(val name: String) : ProfileUiActions()
    data class NumberChanged(val number: String) : ProfileUiActions()
    data class EmailChanged(val email: String) : ProfileUiActions()
    data class IdChanged(val id: String) : ProfileUiActions()
    data object Update : ProfileUiActions()
}

sealed class ProfileUiEvents {
    data object None : ProfileUiEvents()
    data object UpdateSuccess : ProfileUiEvents()
    data class OnError(val message: String) : ProfileUiEvents()
}
