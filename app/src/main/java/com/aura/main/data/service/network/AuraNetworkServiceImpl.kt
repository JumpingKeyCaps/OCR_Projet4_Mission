package com.aura.main.data.service.network

import android.util.Log
import com.aura.main.data.service.network.interfaces.AuraNetworkService
import com.aura.main.data.service.network.interfaces.RetrofitService
import com.aura.main.model.home.UserAccount
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import retrofit2.Retrofit
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * AuraNetworkService implementation class.
 *
 * Redefine all the interface network methods to be use with the instance of retrofit.
 */
class AuraNetworkServiceImpl @Inject constructor(private val retrofitService: RetrofitService) :
    AuraNetworkService {



    /**
     * Methode to login using retrofit the instance.
     *
     * @param loginRequest a login request object.
     * @return  a LoginResponse object with the login access state.
     */
    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        try{
            val response = retrofitService.login(loginRequest)
            Log.d("LOGINdebug", "loginANSimpl reply: ${response.body()!!} ")
            // Check for successful response
            if (response.isSuccessful) {
                return response.body()!! // Assuming successful response has a body
            } else {
                // Handle unsuccessful response from the server
                throw NetworkException.ServerErrorException(response.code())
            }
        }catch (e:Exception){
            //Use of the custom network exceptions method for other network exceptions.
            throw customException(e)
        }
    }

    /**
     * Methode to do a transfer using the retrofit instance.
     *
     * @param transferRequest a transfer request object.
     * @return  a TransferResponse object with the transfer result.
     */
    override suspend fun transfer(transferRequest: TransferRequest): TransferResponse {
        try{
            val response = retrofitService.transfer(transferRequest)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                throw NetworkException.ServerErrorException(response.code())
            }
        }catch (e:Exception){
            throw customException(e)
        }
    }

    /**
     * Methode to get the user accounts using the retrofit instance.
     *
     * @param id of the user.
     * @return  a UserAccount objects list.
     */
    override suspend fun getUserAccounts(id: String): List<UserAccount> {
        try{
            val response = retrofitService.getUserAccounts(id)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                throw NetworkException.ServerErrorException(response.code())
            }
        }catch (e:Exception){
            throw customException(e)
        }
    }


    /**
     * Methode to throw a network custom exceptions.
     *
     * @param e the generic raised exception.
     * @return  Throwable custom network exception.
     */
    private fun customException(e:Exception): Throwable{
        return when (e) {
            is SocketTimeoutException -> NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)
            is ConnectException -> NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)
            else -> NetworkException.UnknownNetworkException()
        }
    }

}