package com.aura.main.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.main.data.repository.HomeRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.model.home.HomeLCE
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
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel()  {

    /** The Home LCE Stateflow. */
    private val _lceState = MutableStateFlow<HomeLCE>(HomeLCE.HomeLoading(R.string.home_conn_loading))
    val lceState: StateFlow<HomeLCE> = _lceState

    /**
     * Method to get the main UserAccount of the user from his user ID.
     *
     * @param idUser the User ID.
     */
    fun getUserAccount(idUser: String){
        viewModelScope.launch {
            //set the state LCE to loading mode
            _lceState.value = HomeLCE.HomeLoading(R.string.home_conn_loading)
            try {
                //call the repository methode to get the user Accounts list
                val userAccounts = homeRepository.getUserAccounts(idUser)
                // Try to the main account from the list returned
                val mainAccount = userAccounts.firstOrNull { it.main }
                if(mainAccount!=null){
                    //SUCCESS ! to fetch the main account
                    //update the state content with the responded user money balance.
                    _lceState.value = HomeLCE.HomeContent(mainAccount.balance)
                }else{
                    //FAIL ! to find main account.
                    _lceState.value = HomeLCE.HomeError(R.string.home_conn_error_failToFindMainAccount)
                }
            } catch (e: Exception) {
                // Handle network errors, server errors, etc.
                when (e) {
                    is NetworkException.ServerErrorException ->{_lceState.value = HomeLCE.HomeError(R.string.conn_error_server_spe)}
                    is NetworkException.NetworkConnectionException  ->{
                        if(e.isSocketTimeout) _lceState.value = HomeLCE.HomeError(R.string.home_conn_error_server)
                        if(e.isConnectFail) _lceState.value = HomeLCE.HomeError(R.string.home_conn_error_network)
                    }
                    is NetworkException.UnknownNetworkException  ->{ _lceState.value = HomeLCE.HomeError(R.string.conn_error_network_generik)}
                    else -> { _lceState.value = HomeLCE.HomeError(R.string.home_conn_error_generik)} // other case of error for login
                }
            }
        }
    }




}