package com.aura.main.data.service

import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    /**
     * Method for a login request to the server (POST)
     *
     * (Use of "suspend" to allow asynchronous call inside coroutine)
     *
     * @param loginRequest a login request object.
     * @return a LoginResponse with a boolean to know if the login is successful or not.
     */
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse


    /**
     * Method for a transfer request to the server (POST)
     *
     * (Use of "suspend" to allow asynchronous call inside coroutine)
     *
     * @param transferRequest a transfer request object.
     * @return a TransferResponse with a boolean to know if the transfer is successful or not.
     */
    @POST("/transfer")
    suspend fun transfer(@Body transferRequest: TransferRequest): TransferResponse






}