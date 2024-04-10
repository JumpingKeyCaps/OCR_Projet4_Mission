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
class AuraApiService  @Inject constructor(private val retrofit: Retrofit) : ApiService {

    /**
     * The ApiService initiate lately with a retrofit instance
     */
    private val apiService by lazy { retrofit.create(ApiService::class.java) }

    /**
     * Methode to login.
     *
     * @param loginRequest a login request object.
     * @return  a LoginResponse object with the login access state.
     */
     override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }

    /**
     * Methode to do a transfer.
     *
     * @param transferRequest a transfer request object.
     * @return  a TransferResponse object with the transfer result.
     */
    override suspend fun transfer(transferRequest: TransferRequest): Response<TransferResponse> {
        return apiService.transfer(transferRequest)
    }

    /**
     * Methode to get the user accounts.
     *
     * @param id of the user.
     * @return  a UserAccount objects list.
     */
    override suspend fun getUserAccounts(id: String): Response<List<UserAccount>> {
        return apiService.getUserAccounts(id)
    }

}