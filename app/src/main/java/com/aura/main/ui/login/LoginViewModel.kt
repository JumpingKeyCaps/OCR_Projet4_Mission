package com.aura.main.ui.login

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.main.data.repository.LoginRepository
import com.aura.main.model.login.LoginLCE
import com.aura.main.model.login.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


/**
 * The ViewModel for the Login Activity. Inject the repository dependency in the constructor
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel()  {

    /** The Login LCE Stateflow. */
    private val _lceState = MutableStateFlow<LoginLCE>(LoginLCE.LoginContent(fieldIsOK = false,granted = false))
    val lceState: StateFlow<LoginLCE> = _lceState


    /**
     * Method to login in the user account.
     *
     * @param identifier the Id of the user.
     * @param password the password of the user.
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun login(identifier: String, password: String){
        // Use viewModelScope for coroutines related to the Activity lifecycle
        viewModelScope.launch {
            _lceState.value = LoginLCE.LoginLoading(R.string.login_conn_running)

            try {
                val loginResponse = loginRepository.login(LoginRequest(identifier, password))

                //todo REMOVE THIS FAKE DELAY --------------
                delay(2000)
                //todo -------------------------------------

                if(loginResponse.granted){
                    // Connexion accepted ! successful login response
                    _lceState.value = LoginLCE.LoginContent(fieldIsOK = true, granted = true)
                }else{
                    //Connexion refused.
                    _lceState.value = LoginLCE.LoginError(R.string.login_conn_fail)
                }
            } catch (e: Exception) {
                // Handle network errors, server errors, etc.
                Log.d("LOGINdebug", "login: ERROR : ${e.toString()} ")

                when (e) {
                    is SocketTimeoutException -> {_lceState.value = LoginLCE.LoginError(R.string.login_conn_error_server)} //server down
                    is ConnectException -> {_lceState.value = LoginLCE.LoginError(R.string.login_conn_error_network)}  // network connexion down
                    else -> { _lceState.value = LoginLCE.LoginError(R.string.login_conn_error_generik)} // other case
                }
            }
        }
    }

    /**
     * Method to check if the user have enter all the required data to login.
     *
     * @param identifier the Id enter by the user.
     * @param password the password enter by the user.
     */
    fun fieldsCheck(identifier: String, password: String){
        if (identifier.isNotEmpty() && password.isNotEmpty()) {
            //champ de saisie Ok !
            _lceState.value = LoginLCE.LoginContent(fieldIsOK = true, granted = false)
        } else {
            //champ de saisie manquant
            _lceState.value = LoginLCE.LoginContent(fieldIsOK = false, granted = false)
        }
    }


}