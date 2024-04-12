package com.aura.main.model.transfer

/**
 * LCE for the transfer activity
 *
 *  Login Content Error pattern to Defines the different states of the transfer activity.
 *
 *  - TransferLoading : the state when the activity is transferring some money.
 *  - TransferSuccess : the state when the activity as successfully transfer the money.
 *  - TransferError : the state when the activity as an error to transfer money.
 */
sealed class TransferLCE {
    data class TransferLoading(val message: Int) : TransferLCE()
    data class TransferContent(val fieldIsOK: Boolean, val result: Boolean) : TransferLCE()
    data class TransferError(val errorMessage: Int) : TransferLCE()
}