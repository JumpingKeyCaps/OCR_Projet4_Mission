package com.aura.main.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * The ViewModel for the Login Activity.
 */
class LoginViewModel : ViewModel()  {

    private val _etat = MutableStateFlow(ConnexionState.INITIAL)
    val etat: StateFlow<ConnexionState> = _etat

    fun verifierChamps(email: String, motDePasse: String) {
        if (email.isNotEmpty() && motDePasse.isNotEmpty()) {
            //champ de saisie Ok !
            _etat.value = ConnexionState.CHAMPS_REMPLIS
        } else {
            //champ de saisie manquant
            _etat.value = ConnexionState.INITIAL
        }
    }

    fun seConnecter() {
        // Use viewModelScope for coroutines related to the Activity lifecycle
        viewModelScope.launch {
            _etat.value = ConnexionState.CONNEXION_EN_COURS
            // Simuler une connexion (could be replaced with actual network call)

            //todochange by apicall
            delay(2000)

            if (true) {
                // Success !
                _etat.value = ConnexionState.CONNEXION_REUSSIE

            } else {
                //erreur de connexion
                _etat.value = ConnexionState.ERREUR_CONNEXION

            }
        }

    }

}