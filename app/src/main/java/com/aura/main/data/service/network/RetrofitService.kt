package com.aura.main.data.service.network

import com.aura.main.model.home.UserAccount
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * The Retrofit  Service Interface to communicate with the server.
 */
interface RetrofitService {

    /**
     * Method for a login request to the server (POST)
     *
     * (Use of "suspend" to allow asynchronous call inside coroutine)
     *
     * @param loginRequest a login request object.
     * @return a LoginResponse with a boolean to know if the login is successful or not.
     */
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


    /**
     * Method for a transfer request to the server (POST)
     *
     * (Use of "suspend" to allow asynchronous call inside coroutine)
     *
     * @param transferRequest a transfer request object.
     * @return a TransferResponse with a boolean to know if the transfer is successful or not.
     */
    @POST("/transfer")
    suspend fun transfer(@Body transferRequest: TransferRequest): Response<TransferResponse>


    /**
     * Method to get all data user's accounts. (GET)
     *
     * (Use of "suspend" to allow asynchronous call inside coroutine)
     *
     * @param id the id of the user.
     * @return a list of UserAccount objects with all the data of the account.
     */
    @GET("/accounts/{id}")
    suspend fun getUserAccounts(@Path("id") id: String): Response<List<UserAccount>>


}