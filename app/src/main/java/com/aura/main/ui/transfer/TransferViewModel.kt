package com.aura.main.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.main.data.repository.TransferRepository
import com.aura.main.model.transfer.TransferLCE
import com.aura.main.model.transfer.TransferRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * The ViewModel for the Transfer Activity.  Inject the repository dependency in the constructor
 */
@HiltViewModel
class TransferViewModel @Inject constructor(private val transferRepository: TransferRepository) : ViewModel()  {

    /** The Transfer LCE Stateflow. */
    private val _lceState = MutableStateFlow<TransferLCE>(TransferLCE.TransferContent(fieldIsOK = false, result = false))
    val lceState: StateFlow<TransferLCE> = _lceState


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
     * @param senderId the Id of the user.
     * @param receiverId the target account Id.
     * @param amount the money to transfer.
     */
    fun transfer(senderId: String, receiverId: String, amount: String) {


        // Use viewModelScope for coroutines related to the Activity lifecycle
        viewModelScope.launch {
            _lceState.value = TransferLCE.TransferLoading(R.string.transfer_conn_loading)
            try {
                val transferResponse = transferRepository.transfer(TransferRequest(senderId, receiverId,amount.toDouble()))

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
                    is SocketTimeoutException -> {_lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_server)} //server down
                    is ConnectException -> {_lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_network)}  // network connexion down
                    else -> { _lceState.value = TransferLCE.TransferError(R.string.transfer_conn_error_generik)} // other case
                }
            }
        }
    }


}