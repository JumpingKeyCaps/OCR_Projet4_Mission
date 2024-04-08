package com.aura.main.data.repository

import com.aura.main.data.service.AuraApiService
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val auraApiService: AuraApiService) {

    suspend fun login(user: LoginRequest): LoginResponse {
        return auraApiService.login(user)
    }

}