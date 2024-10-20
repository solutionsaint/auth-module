package com.techlambda.authlibrary.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlambda.authlibrary.ui.signUp.UserRepository
import com.techlambda.authlibrary.ui.utils.NetworkResult
import com.techlambda.authlibrary.ui.utils.setLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel@Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiEvents = Channel<SplashScreenUiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onEvent(event: SplashScreenUiActions) {
        when (event) {
            is SplashScreenUiActions.SendProjectId -> sendProject(event.projectId)
            SplashScreenUiActions.ClearError -> clearError()
        }
    }

    private fun sendProject(projectId: String) {
        viewModelScope.launch {
            setLoading(true)
            val response = repository.sendProjectId(
                projectId = projectId
            )
            Log.d("SplashScreenViewModel", "API Response: $response")
            when (response) {
                is NetworkResult.Error -> {
                    setLoading(false)
                    _uiEvents.send(SplashScreenUiEvents.OnError(response.message ?: ""))
                }

                is NetworkResult.Success -> {
                    setLoading(false)
                    response.data?.let {
                        _uiEvents.send(SplashScreenUiEvents.Success)
                    }
                }
            }
        }
    }

    private fun clearError() {
        viewModelScope.launch {
            _uiEvents.send(SplashScreenUiEvents.None)
        }
    }
}

    sealed class SplashScreenUiActions {
        data class SendProjectId(val projectId: String) : SplashScreenUiActions()
        data object ClearError : SplashScreenUiActions()
    }

    sealed class SplashScreenUiEvents {
        data object None : SplashScreenUiEvents()
        data object Success : SplashScreenUiEvents()
        data class OnError(val message: String) : SplashScreenUiEvents()
    }
