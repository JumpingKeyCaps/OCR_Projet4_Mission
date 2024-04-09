package com.aura.main.data.repository

import com.aura.main.data.service.AuraApiService
import com.aura.main.model.home.UserAccount
import com.google.gson.Gson
import retrofit2.Response
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
            //on deserialise la Json en une list de UserAccount
            val gson = Gson()
            //on return la reponse avec notre list.
            return gson.fromJson(response.body()?.toString(), Array<UserAccount>::class.java).toList()
        }

    }

}