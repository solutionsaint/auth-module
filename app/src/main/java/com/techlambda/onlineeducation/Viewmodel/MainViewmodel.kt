package com.techlambda.onlineeducation.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.techlambda.onlineeducation.Repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val authRepository: MainRepository
) : ViewModel() {

    fun login(username: String, password: String, email: String) = liveData(Dispatchers.IO) {
        emit(Result.success(MainViewmodel(authRepository)))
        val result = authRepository.login(username, password, email)
        emit(result)
    }
}