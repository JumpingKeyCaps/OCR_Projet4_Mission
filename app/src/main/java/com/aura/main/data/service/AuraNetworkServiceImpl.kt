package com.aura.main.data.service

import com.aura.main.model.home.UserAccount
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import retrofit2.Retrofit
import javax.inject.Inject

class AuraNetworkServiceImpl @Inject constructor(private val retrofit: Retrofit) : AuraNetworkService {

    private val apiService by lazy { retrofit.create(ApiService::class.java) }

    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        val response = apiService.login(loginRequest)
        // Check for successful response
        if (response.isSuccessful) {
            return response.body()!! // Assuming successful response has a body
        } else {
            // Handle unsuccessful response (throw exception, log error, etc.)
            throw Exception("Login failed: ${response.code()}")
        }
    }

    override suspend fun transfer(transferRequest: TransferRequest): TransferResponse {
        val response = apiService.transfer(transferRequest)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Transfer failed: ${response.code()}")
        }

    }

    override suspend fun getUserAccounts(id: String): List<UserAccount> {
        val response = apiService.getUserAccounts(id)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Get user accounts failed: ${response.code()}")
        }

    }
}