package com.aura.main.model.transfer

/**
 * Data class who represent a Transfer Response data from the server.
 *
 * @param result the reply from the server if the transfer is successful or not.
 */
data class TransferResponse(val result: Boolean)