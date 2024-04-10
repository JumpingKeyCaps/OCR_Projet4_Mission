package com.aura.main.data.repository

import com.aura.main.data.service.AuraApiService
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import javax.inject.Inject

/**
 * Repository for the transfer data.
 * Inject the used API via constructor.
 */
class TransferRepository @Inject constructor(private val auraApiService: AuraApiService) {

    /**
     * Method to do a transfer.
     *
     *  @param transferRequest a transfer request object.
     *  @return  a TransferResponse object with the transfer result.
     */
    suspend fun transfer(transferRequest: TransferRequest): TransferResponse {
        val response = auraApiService.transfer(transferRequest)
        return response.body() ?: TransferResponse(result = false)
    }

}