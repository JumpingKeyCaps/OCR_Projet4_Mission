package com.aura.main.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.main.data.repository.HomeRepository
import com.aura.main.model.home.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the Home Activity.
 * Inject the repository dependency in the constructor
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel()  {
    /** The Home state Stateflow. */
    private val _etat = MutableStateFlow(HomeState.IDLE)
    val etat: StateFlow<HomeState> = _etat

    /**
     * The User Account StateFlow
     */
    private val _userAccount = MutableStateFlow<UserAccount?>(null)
    val userAccount: StateFlow<UserAccount?> = _userAccount


    /**
     * Method to get the UserAccount from the user ID
     *
     * @param idUser the User ID.
     */
    fun getUserAccounts(idUser: String) {
        viewModelScope.launch {
            _etat.value = HomeState.LOADING

            //todo DEBUG ONLY -------------
                delay(3000)
            //todo --------------------

            try {
                //call the repository methode to get the user Accounts list
                val userAccounts = homeRepository.getUserAccounts(idUser)
                // get the main account from the list returned
                val mainAccount = userAccounts.firstOrNull { it.main }
                _userAccount.value = mainAccount
                if(mainAccount!=null){
                    _etat.value = HomeState.SUCCESS
                }else{
                    _etat.value = HomeState.ERROR
                }
            } catch (e: Exception) {
                _etat.value = HomeState.ERROR
            }
        }
    }


    /**
     * Method to get the informative message to display to the user.
     * @param homeState the current home state of the activity.
     * @return the reference of the string resource to use.
     */
    @StringRes
    fun getHomeInfoMessageToShow(homeState: HomeState): Int {
        return when(homeState){
            HomeState.LOADING -> R.string.home_conn_loading
            HomeState.ERROR -> R.string.home_conn_error
            else -> 0
        }
    }

}