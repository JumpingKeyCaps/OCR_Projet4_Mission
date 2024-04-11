package com.aura.main.ui.transfer

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.main.data.repository.TransferRepository
import com.aura.main.model.transfer.TransferRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the Transfer Activity.  Inject the repository dependency in the constructor
 */
@HiltViewModel
class TransferViewModel @Inject constructor(private val transferRepository: TransferRepository) : ViewModel()  {

    /** The transfer state Stateflow. */
    private val _etat = MutableStateFlow(TransferState.IDLE)
    val etat: StateFlow<TransferState> = _etat


    /**
     * Method to check if the user have enter all the required data to the Transfer.
     *
     * @param receiverID the target Id account to the transfer.
     * @param amount the ammount of money to transfer.
     */
    fun verifierTransferChamps(receiverID: String, amount: String) {
        if (receiverID.isNotEmpty() && amount.isNotEmpty()) {
            try{
                amount.toDouble()
                //champ de saisie Ok !
                _etat.value = TransferState.FIELDS_OK
            }catch (e:NumberFormatException ){
                //champ de saisie manquant
                _etat.value = TransferState.IDLE
            }
        } else {
            //champ de saisie manquant
            _etat.value = TransferState.IDLE
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
            _etat.value = TransferState.LOADING
            try {
                val transferResponse = transferRepository.transfer(TransferRequest(senderId, receiverId,amount.toDouble()))

                //todo REMOVE THIS FAKE DELAY --------------
                   delay(3000)
                //todo -------------------------------------

                if(transferResponse.result){
                    // transfert accepted !
                    _etat.value = TransferState.SUCCESS
                }else{
                    //transfer refused.
                    _etat.value = TransferState.FAIL
                }
            } catch (e: Exception) {
                // Handle network errors, server errors, etc.
                _etat.value = TransferState.ERROR
            }
        }
    }


    /**
     * Method to get the transfer informative message to display to the user.
     * @param transferState the current login state of the activity.
     */
    @StringRes
    fun getTransferInfoMessageToShow(transferState: TransferState): Int {
        return when(transferState){
            TransferState.ERROR -> R.string.transfer_conn_error
            TransferState.FAIL -> R.string.transfer_conn_fail
            TransferState.LOADING -> R.string.transfer_conn_loading
            else -> 0
        }
    }

}