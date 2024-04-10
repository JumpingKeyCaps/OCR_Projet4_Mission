package com.aura.main.data.repository

import com.aura.main.data.service.AuraApiService
import com.aura.main.model.home.UserAccount
import javax.inject.Inject

/**
 * Repository for the Home data.
 * Inject the used API via constructor.
 */
class HomeRepository @Inject constructor(private val auraApiService: AuraApiService) {

    /**
     * Method to get all the user accounts.
     *
     * @param id the user identifier.
     * @return a list of the user accounts.
     */
    suspend fun getUserAccounts(id: String): List<UserAccount> {
        //get the service response
        val response = auraApiService.getUserAccounts(id)
        if (!response.isSuccessful) {
            //fail
            throw Exception("Error: ${response.code()}")
        }else{
            //success
            return response.body()?: emptyList()
        }
    }

}