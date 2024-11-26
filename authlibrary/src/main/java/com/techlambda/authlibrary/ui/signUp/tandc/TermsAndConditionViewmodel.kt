package com.techlambda.authlibrary.ui.signUp.tandc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.signUp.UserRepository
import com.techlambda.authlibrary.ui.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsAndConditionViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiStates = MutableStateFlow(TermsAndConditionUiState())
    val state = _uiStates.asStateFlow()

    private val _uiEvents = Channel<TermsAndConditionUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun getTermsAndCondition() {
        viewModelScope.launch {
            val response = repository.termsAndCondition()

            when (response) {
                is NetworkResult.Error -> {
                    _uiEvents.send(TermsAndConditionUiEvents.OnError("An error occurred during terms and condition: ${response.message}"))
                }

                is NetworkResult.Success -> {
                    _uiStates.update { _uiStates.value.copy(termsAndCondition = response.data?.data?.discription) }
                }
            }
        }
    }
}

data class TermsAndConditionUiState(
    val termsAndCondition: String? = "",
)


sealed class TermsAndConditionUiEvents {
    data object None : TermsAndConditionUiEvents()
    data class OnError(val message: String) : TermsAndConditionUiEvents()
}
