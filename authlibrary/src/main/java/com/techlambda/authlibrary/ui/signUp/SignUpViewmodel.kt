package com.techlambda.authlibrary.ui.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.models.FilterData
import com.techlambda.authlibrary.ui.models.FilterRequest
import com.techlambda.authlibrary.ui.models.SignUpRequest
import com.techlambda.authlibrary.ui.models.ValueData
import com.techlambda.authlibrary.ui.network.repo.CommonRepository
import com.techlambda.authlibrary.ui.utils.NetworkResult
import com.techlambda.authlibrary.ui.utils.isValidEmail
import com.techlambda.authlibrary.ui.utils.isValidPhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
    private val commonRepo: CommonRepository
) : ViewModel() {

    private val _uiStates = MutableStateFlow(SignUpUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<SignUpUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()


    fun onEvent(event: SignUpUiActions) {
        when (event) {
            is SignUpUiActions.NameChanged -> {
                _uiStates.update {
                    it.copy(name = event.name)
                }
            }

            is SignUpUiActions.EmailChanged -> {
                _uiStates.value = _uiStates.value.copy(email = event.email)
            }

            is SignUpUiActions.NumberChanged -> {
                _uiStates.value = _uiStates.value.copy(number = event.number)
            }

            is SignUpUiActions.PasswordChanged -> {
                _uiStates.value = _uiStates.value.copy(password = event.password)
            }

            is SignUpUiActions.ConfirmPasswordChanged -> {
                _uiStates.value = _uiStates.value.copy(confirmPassword = event.confirmPassword)
            }

            is SignUpUiActions.UserTypeChanged -> {
                _uiStates.value = _uiStates.value.copy(userType = event.userType)
            }

            is SignUpUiActions.SignUp -> {
                signUp()
            }

            is SignUpUiActions.TermsAndConditionChanged -> {
                _uiStates.value = _uiStates.value.copy(termsAndCondition = event.termsAndCondition)
            }

            is SignUpUiActions.UpdateAppId -> {
                _uiStates.value = _uiStates.value.copy(appId = event.appId)
            }

            is SignUpUiActions.UpdateToken -> {
                _uiStates.update { _uiStates.value.copy(token = event.token) }
            }
        }
    }


    private fun signUp() {
        viewModelScope.launch {
            val req = SignUpRequest(
                name = _uiStates.value.name,
                phone = _uiStates.value.number,
                email = _uiStates.value.email,
                password = _uiStates.value.password,
                userType = _uiStates.value.userType,
                fcmToken = _uiStates.value.token!!,
                appId = _uiStates.value.appId
            )
            Log.d("TAG", "signUp: $req")
            val response = repository.signUp(
                req
            )

            when (response) {
                is NetworkResult.Error -> {
                    _uiEvents.send(SignUpUiEvents.OnError("An error occurred during signup: ${response.message}"))
                }

                is NetworkResult.Success -> {
                    _uiEvents.send(SignUpUiEvents.SignUpSuccess(response.message ?: ""))
                }
            }
        }
    }

    fun masterFilter() {
        viewModelScope.launch {
            val req = FilterRequest(
                filter = FilterData(
                    value = ValueData(
                        field = "type",
                        op = "=",
                        value = "role"
                    )
                )
            )
            Log.d("TAG", "commonFilter: $req")
            val response = commonRepo.masterFilter(
                req
            )

            when (response) {
                is NetworkResult.Error -> {
                    _uiEvents.send(SignUpUiEvents.OnError("An error occurred while fetching user roles: ${response.message}"))
                }

                is NetworkResult.Success -> {
                    val userRolesList = ArrayList<String>()
                    response.data?.data?.forEach{
                        userRolesList.add(it.title)
                    }
                    _uiStates.value = _uiStates.value.copy(userRoles = userRolesList)
                }
            }
        }
    }

    fun validateSignUp(): String {
        return when {
            _uiStates.value.name.isEmpty() -> "Please enter Name"
            _uiStates.value.email.isEmpty() -> "Please enter Email Address"
            _uiStates.value.number.isEmpty() -> "Please enter Mobile Number"
            _uiStates.value.password.isEmpty() -> "Please enter Password"
            _uiStates.value.confirmPassword.isEmpty() -> "Please enter Confirm Password"
            _uiStates.value.password != _uiStates.value.confirmPassword -> "Passwords do not match"
            !_uiStates.value.termsAndCondition -> "Please accept terms and conditions"
            !isValidEmail(_uiStates.value.email) -> "Please enter valid email address"
            !isValidPhoneNumber(_uiStates.value.number) -> "Please enter valid mobile number"
            _uiStates.value.token.isNullOrBlank() -> "Something went wrong. Please re-install"
            else -> "Validated"
        }
    }
}

data class SignUpUiState(
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val appId: String = "",
    val password: String = "",
    val token: String? = null,
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val termsAndCondition: Boolean = true,
    val userType: String = "",
    val userRoles: List<String> = emptyList()
)

sealed class SignUpUiActions {
    data class NameChanged(val name: String) : SignUpUiActions()
    data class NumberChanged(val number: String) : SignUpUiActions()
    data class EmailChanged(val email: String) : SignUpUiActions()
    data class TermsAndConditionChanged(val termsAndCondition: Boolean) : SignUpUiActions()
    data class PasswordChanged(val password: String) : SignUpUiActions()
    data class UpdateAppId(val appId: String) : SignUpUiActions()
    data class UpdateToken(val token: String?) : SignUpUiActions()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpUiActions()
    data object SignUp : SignUpUiActions()
    data class UserTypeChanged(val userType: String) : SignUpUiActions()
}

sealed class SignUpUiEvents {
    data object None : SignUpUiEvents()
    data class SignUpSuccess(val message: String) : SignUpUiEvents()
    data class OnError(val message: String) : SignUpUiEvents()
}
