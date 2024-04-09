package com.aura.main.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.main.data.repository.HomeRepository
import com.aura.main.model.home.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the Home Activity.
 * - Inject the repository dependency in the constructor
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel()  {
    /** The Home state Stateflow. */
    private val _etat = MutableStateFlow(HomeState.IDLE)
    val etat: StateFlow<HomeState> = _etat


    private val _userAccount = MutableStateFlow<UserAccount?>(null)
    val userAccount: StateFlow<UserAccount?> = _userAccount

    fun getUserAccounts(idUser: String) {
        viewModelScope.launch {
            _etat.value = HomeState.LOADING
            try {
                val userAccounts = homeRepository.getUserAccounts(idUser)

                val mainAccount = userAccounts.firstOrNull { it.main }
                _userAccount.value = mainAccount
                _etat.value = HomeState.SUCCESS
                Log.d("Homedebug", "ViewModel Success ! : ${mainAccount.toString()}")
            } catch (e: Exception) {
                _etat.value = HomeState.ERROR
                Log.d("Homedebug", "ViewModel Error Exception ! : ${e.toString()}")

            }
        }
    }
}