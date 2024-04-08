package com.aura.main.data.repository

import com.aura.main.data.service.LoginApiService
import com.aura.main.model.LoginRequest
import com.aura.main.model.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginApiService: LoginApiService) {

    suspend fun login(user: LoginRequest): LoginResponse {
        return loginApiService.login(user)
    }

}