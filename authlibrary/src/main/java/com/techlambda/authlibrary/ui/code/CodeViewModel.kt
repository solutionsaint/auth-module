package com.techlambda.authlibrary.ui.code

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.models.CodeVerificationRequest
import com.techlambda.authlibrary.ui.models.CodeVerificationResponse
import com.techlambda.authlibrary.ui.signUp.UserRepository
import com.techlambda.authlibrary.ui.utils.NetworkResult
import com.techlambda.authlibrary.ui.utils.setLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodeViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiStates = MutableStateFlow(CodeScreenUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<CodeScreenUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onEvent(events: CodeScreenUiActions) {
        when(events) {
            is CodeScreenUiActions.CodeChanged -> {
                _uiStates.update { it.copy(code = events.code) }
            }
            is CodeScreenUiActions.UserIdChanged -> {
                _uiStates.update { it.copy(userId = events.userId) }
            }
            is CodeScreenUiActions.Submit -> {
                submit()
            }
            is CodeScreenUiActions.ClearError -> {
                clearError()
            }
        }
    }

    private fun submit() {
        viewModelScope.launch {
            setLoading(true)
            val response = repository.verifyCode(
                CodeVerificationRequest(
                    userId = _uiStates.value.userId,
                    code = _uiStates.value.code
                )
            )
            Log.d("CodeViewModel", "API Response: $response")
            when (response) {
                is NetworkResult.Error -> {
                    setLoading(false)
                    _uiEvents.send(CodeScreenUiEvents.OnError(response.message ?: ""))
                }

                is NetworkResult.Success -> {
                    setLoading(false)
                    response.data?.data?.let {
                        _uiEvents.send(CodeScreenUiEvents.CodeValidationSuccess(it))
                    }
                }
            }
        }
    }

    private fun clearError() {
        viewModelScope.launch {
            _uiEvents.send(CodeScreenUiEvents.None)
        }
    }
}

data class CodeScreenUiState(
    val code: String = "",
    val userId: String = "",
    val isLoading: Boolean = false
)

sealed class CodeScreenUiEvents {
    data object None : CodeScreenUiEvents()
    data class CodeValidationSuccess(val codeVerificationResponse: CodeVerificationResponse) : CodeScreenUiEvents()
    data class OnError(val message: String) : CodeScreenUiEvents()
}

sealed class CodeScreenUiActions{
    data class CodeChanged(val code: String) : CodeScreenUiActions()
    data class UserIdChanged(val userId: String) : CodeScreenUiActions()
    data object Submit : CodeScreenUiActions()
    data object ClearError : CodeScreenUiActions()
}