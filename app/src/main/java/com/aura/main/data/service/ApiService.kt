package com.aura.main.data.service

import com.aura.main.model.LoginRequest
import com.aura.main.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}