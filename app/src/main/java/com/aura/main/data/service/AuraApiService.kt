package com.aura.main.data.service

import com.aura.main.model.home.UserAccount
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import retrofit2.Retrofit
import retrofit2.Response
import javax.inject.Inject

/**
 * The app API service to communicate with the server.
 */
class AuraApiService  @Inject constructor(private val auraNetworkService: AuraNetworkServiceImpl) {


    /**
     * Methode to login.
     *
     * @param loginRequest a login request object.
     * @return  a LoginResponse object with the login access state.
     */
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return auraNetworkService.login(loginRequest)
    }

    /**
     * Methode to do a transfer.
     *
     * @param transferRequest a transfer request object.
     * @return  a TransferResponse object with the transfer result.
     */
    suspend fun transfer(transferRequest: TransferRequest): TransferResponse {
        return auraNetworkService.transfer(transferRequest)
    }

    /**
     * Methode to get the user accounts.
     *
     * @param id of the user.
     * @return  a UserAccount objects list.
     */
    suspend fun getUserAccounts(id: String): List<UserAccount> {
        return auraNetworkService.getUserAccounts(id)
    }

}