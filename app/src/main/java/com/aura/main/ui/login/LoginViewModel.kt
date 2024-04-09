package com.aura.main.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.main.data.repository.LoginRepository
import com.aura.main.model.login.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * The ViewModel for the Login Activity.
 * - Inject the repository dependency in the constructor
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel()  {

    /** The Login state Stateflow. */
    private val _etat = MutableStateFlow(ConnexionState.INITIAL)
    val etat: StateFlow<ConnexionState> = _etat

    /**
     * Method to check if the user have enter all the required data to login.
     *
     * @param identifier the Id enter by the user.
     * @param motDePasse the password enter by the user.
     */
    fun verifierChamps(identifier: String, motDePasse: String) {
        if (identifier.isNotEmpty() && motDePasse.isNotEmpty()) {
            //champ de saisie Ok !
            _etat.value = ConnexionState.CHAMPS_REMPLIS
        } else {
            //champ de saisie manquant
            _etat.value = ConnexionState.INITIAL
        }
    }

    /**
     * Method to login the user account.
     *
     * @param identifier the Id of the user.
     * @param motDePasse the password of the user.
     */
    fun seConnecter(identifier: String, motDePasse: String) {
        // Use viewModelScope for coroutines related to the Activity lifecycle
        viewModelScope.launch {
            _etat.value = ConnexionState.CONNEXION_EN_COURS


            //todo REMOVE THAT IT ONLY TO TEST ___________________
            delay(10000)
            //todo --------------------------------------------------
            try {
                val loginResponse = loginRepository.login(LoginRequest(identifier, motDePasse))

                if(loginResponse.granted){
                    // Connexion accepted ! successful login response
                    _etat.value = ConnexionState.CONNEXION_REUSSIE
                    Log.d("connectMMM", "Connexion Ok")
                }else{
                    //Connexion refused.
                    _etat.value = ConnexionState.CONNEXION_ECHEC
                    Log.d("connectMMM", "seConnecter: echec de connexion !")
                }

            } catch (e: Exception) {
                // Handle network errors, server errors, etc.
                _etat.value = ConnexionState.ERREUR_CONNEXION
                Log.d("connectMMM", "seConnecter: erreur rsx ! \n"+e.message+"\n"+e.toString())
            }


        }

    }

}