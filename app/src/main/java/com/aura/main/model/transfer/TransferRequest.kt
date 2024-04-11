package com.aura.main.model.transfer

import com.squareup.moshi.Json


/**
 * Data class who represent a transfer request data.
 *
 * @param sender  The user id who send the transfer.
 * @param recipient  The target user id of the transfer.
 * @param amount The quantity of money to transfer.
 */
data class TransferRequest(
    @Json(name = "sender") val sender: String,
    @Json(name = "recipient") val recipient: String,
    @Json(name = "amount") val amount: Double)