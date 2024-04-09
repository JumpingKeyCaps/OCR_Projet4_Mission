package com.aura.main.data.repository

import android.util.Log
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
        //on recupere la reponse du service
        val response = auraApiService.getUserAccounts(id)
        if (!response.isSuccessful) {
            //echec de la reponse
            throw Exception("Error: ${response.code()}")
        }else{
            //reponse ok
            return response.body()?: emptyList()
        }

    }

}