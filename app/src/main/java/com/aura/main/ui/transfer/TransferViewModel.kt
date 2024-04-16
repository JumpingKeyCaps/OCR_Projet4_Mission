package com.aura.main.ui.transfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.main.data.repository.TransferRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.model.transfer.TransferLCE
import com.aura.main.model.transfer.TransferRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the Transfer Activity.  Inject the repository dependency in the constructor
 */
@HiltViewModel
class TransferViewModel @Inject constructor(private val transferRepository: TransferRepository, private val savedStateHandle: SavedStateHandle) : ViewModel()  {

    /**
     * The user Id
     */
    var userId: String
    /**
     * The user key ref
     */
    private val IDUSER = "userId"

    /** The Transfer LCE Stateflow. */
    private val _lceState = MutableStateFlow<TransferLCE>(TransferLCE.TransferContent(fieldIsOK = false, result = false))
    val lceState: StateFlow<TransferLCE> = _lceState




    init {
        val savedUserId = savedStateHandle.get<String>(IDUSER) ?: ""
        userId = savedUserId
    }

    fun updateUserId(userId: String) {
        this.userId = userId
        savedStateHandle[IDUSER] = userId
    }



    /**
     * Method to check if the user have enter all the required data to the Transfer.
     *
     * @param receiverID the target Id account to the transfer.
     * @param amount the ammount of money to transfer.
     */
    fun fieldsCheck(receiverID: String, amount: String){
        if (receiverID.isNotEmpty() && amount.isNotEmpty()) {
            try{
                amount.toDouble()
                //champ de saisie Ok !
                _lceState.value = TransferLCE.TransferContent(fieldIsOK = true, result = false)
            }catch (e:NumberFormatException ){
                //champ de saisie manquant
                _lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_amount)
            }
        } else {
            //champ de saisie manquant
            _lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_missingfields)
        }
    }


    /**
     * Method to Transfer the user money.
     *
     * @param receiverId the target account Id.
     * @param amount the money to transfer.
     */
    fun transfer(receiverId: String, amount: String) {
        // Use viewModelScope for coroutines related to the Activity lifecycle
        viewModelScope.launch {
            _lceState.value = TransferLCE.TransferLoading(R.string.transfer_conn_loading)
            try {
                val transferResponse = transferRepository.transfer(TransferRequest(userId, receiverId,amount.toDouble()))

                if(transferResponse.result){
                    // transfert accepted !
                    _lceState.value = TransferLCE.TransferContent(fieldIsOK = true, result = true)
                }else{
                    //transfer refused.
                    _lceState.value = TransferLCE.TransferError(R.string.transfer_conn_fail)
                }
            } catch (e: Exception) {
                // Handle network errors, server errors, etc.
                when (e) {
                    is NetworkException.ServerErrorException ->{_lceState.value = TransferLCE.TransferError(R.string.conn_error_server_spe)}
                    is NetworkException.NetworkConnectionException  ->{
                        if(e.isSocketTimeout) _lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_server)
                        if(e.isConnectFail) _lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_network)
                    }
                    is NetworkException.UnknownNetworkException  ->{ _lceState.value = TransferLCE.TransferError(R.string.conn_error_network_generik)}
                    else -> { _lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_generik)} // other case of error for login
                }
            }
        }
    }


}