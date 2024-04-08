package com.aura.main.model.transfer


/**
 * Data class who represent a transfer request data.
 *
 * @param sender  The user id who send the transfer.
 * @param recipient  The target user id of the transfer.
 * @param amount The quantity of money to transfer.
 */
data class TransferRequest(val sender: String, val recipient: String, val amount: Double)