package com.aura.main.data.service.network

import com.aura.main.model.home.UserAccount
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse

/**
 * AuraNetworkService interface with all the network methods.
 */
interface AuraNetworkService {

    suspend fun login(loginRequest: LoginRequest): LoginResponse

    suspend fun transfer(transferRequest: TransferRequest): TransferResponse

    suspend fun getUserAccounts(id: String): List<UserAccount>
}