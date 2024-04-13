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

    class ServerErrorException(val errorCode: Int) : NetworkException() // the errorCode can be used for more accuracy on the server error message to display (not used in my case).

    class NetworkConnectionException(val isSocketTimeout: Boolean, val isConnectFail: Boolean) : NetworkException()

    class UnknownNetworkException() : NetworkException() // I let an empty constructor free to be customised in case of new unexpected exception in the future of the code evolution.
}