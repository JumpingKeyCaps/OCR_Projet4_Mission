package com.aura.main.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.main.data.repository.HomeRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.di.AppConstants
import com.aura.main.model.ScreenState
import com.aura.main.model.home.HomeContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the Home Activity.
 * Inject the repository dependency in the constructor
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository, private val savedStateHandle: SavedStateHandle) : ViewModel()  {


    /**
     * The user Id
     */
    var userId: String



    /** The Home LCE Stateflow. */
    private val _lceState = MutableStateFlow<ScreenState<HomeContent>>(ScreenState.Loading(R.string.home_conn_loading))
    val lceState: StateFlow<ScreenState<HomeContent>> = _lceState


    /**
     * Initialisation du ViewModel
     */
    init {
        val savedUserId = savedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) ?: ""
        userId = savedUserId
    }


    /**
     * Method to update the userId ans save it in the savedStateHandle.
     *
     * @param userId the new userid to update.
     */
    fun updateUserId(userId: String) {
        this.userId = userId
        savedStateHandle[AppConstants.Keys.KEY_USER_ID] = userId
    }


    /**
     * Method to get the main UserAccount of the user from his user ID.
     *
     */
    fun getUserAccount(){

        viewModelScope.launch {
            //set the state LCE to loading mode
            _lceState.value = ScreenState.Loading(R.string.home_conn_loading)
            try {
                //call the repository methode to get the user Accounts list
                val userAccounts = homeRepository.getUserAccounts(userId)
                // Try to the main account from the list returned
                val mainAccount = userAccounts.firstOrNull { it.main }
                if(mainAccount!=null){
                    //SUCCESS ! to fetch the main account
                    //update the state content with the responded user money balance.
                    _lceState.value = ScreenState.Content(HomeContent(userBalance = mainAccount.balance))
                }else{
                    //FAIL ! to find main account.
                    _lceState.value = ScreenState.Error(R.string.home_conn_error_failToFindMainAccount)
                }
            } catch (e: Exception) {
                // Handle network errors, server errors, etc.
                when (e) {
                    is NetworkException.ServerErrorException ->{_lceState.value = ScreenState.Error(R.string.conn_error_server_spe)}
                    is NetworkException.NetworkConnectionException  ->{
                        if(e.isSocketTimeout) _lceState.value = ScreenState.Error(R.string.home_conn_error_server)
                        if(e.isConnectFail) _lceState.value = ScreenState.Error(R.string.home_conn_error_network)
                    }
                    is NetworkException.UnknownNetworkException  ->{ _lceState.value = ScreenState.Error(R.string.conn_error_network_generik)}
                    else -> { _lceState.value = ScreenState.Error(R.string.home_conn_error_generik)} // other case of error for login
                }
            }
        }
    }




}