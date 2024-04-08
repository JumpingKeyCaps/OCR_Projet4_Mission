package com.aura.main.data.service

import com.aura.main.model.LoginRequest
import com.aura.main.model.LoginResponse
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Service to communicate with the server.
 */
class LoginApiService  @Inject constructor(private val retrofit: Retrofit) : ApiService {

    override suspend fun login(user: LoginRequest): LoginResponse {
        return apiService.login(user)
    }

    private val apiService by lazy { retrofit.create(ApiService::class.java) }

}