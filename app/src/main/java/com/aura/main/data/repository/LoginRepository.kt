package com.aura.main.data.repository

import com.aura.main.data.service.AuraApiService
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import javax.inject.Inject

/**
 * Repository for the Login data.
 * Inject the used API via constructor.
 */
class LoginRepository @Inject constructor(private val auraApiService: AuraApiService) {

    /**
     * Method to login the user to his account.
     *
     * @param user a LoginRequest object.
     * @return a LoginResponse object.
     */
    suspend fun login(user: LoginRequest): LoginResponse {
        val response = auraApiService.login(user)
        return response.body() ?: LoginResponse(granted = false)
    }

}