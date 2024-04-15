package com.aura.main.data.service.network

/**
 * A Class to Handling all the potential network exceptions.
 *
 *
 *  - ServerErrorException : this is for all server side errors.
 *  - NetworkConnectionException : this is for all network connectivity errors.
 *  - UnknownNetworkException : this is for all other errors.
 */
sealed class NetworkException : Exception(){

    data class ServerErrorException(val errorCode: Int) : NetworkException() // the errorCode can be used for more accuracy on the server error message to display (not used in my case).

    data class NetworkConnectionException(val isSocketTimeout: Boolean, val isConnectFail: Boolean) : NetworkException()

    object  UnknownNetworkException: NetworkException()
}