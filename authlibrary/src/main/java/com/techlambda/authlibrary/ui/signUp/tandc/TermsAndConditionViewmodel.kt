package com.techlambda.authlibrary.ui.signUp.tandc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.models.SignUpRequest
import com.techlambda.authlibrary.ui.signUp.SignUpUiActions
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
class TermsAndConditionViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiStates = MutableStateFlow(TermsAndConditionUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<TermsAndConditionUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()


    fun onEvent(event: TermsAndConditionUiActions) {
        when (event) {
            is TermsAndConditionUiActions.SignUpStatusChanged -> _uiStates.update {
                _uiStates.value.copy(
                    isSignUp = event.status
                )
            }
        }
    }


    private fun signUp() {
        viewModelScope.launch {
            val response = repository.termsAndCondition()

            when (response) {
                is NetworkResult.Error -> {
                    _uiEvents.send(TermsAndConditionUiEvents.OnError("An error occurred during terms and condition: ${response.message}"))
                }

                is NetworkResult.Success -> {
                    _uiStates.update { _uiStates.value.copy(termsAndCondition = response.data?.data) }
                }
            }
        }
    }
}

data class TermsAndConditionUiState(
    val termsAndCondition: String? = "",
    val isAccepted: Boolean = false,
    val isSignUp: Boolean = true
)

sealed class TermsAndConditionUiActions {
    data class SignUpStatusChanged(val status: Boolean) : TermsAndConditionUiActions()
}

sealed class TermsAndConditionUiEvents {
    data object None : TermsAndConditionUiEvents()
    data object Accept : TermsAndConditionUiEvents()
    data class OnError(val message: String) : TermsAndConditionUiEvents()
}
