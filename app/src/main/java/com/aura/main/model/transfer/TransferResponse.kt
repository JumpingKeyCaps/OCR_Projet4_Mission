package com.aura.main.model.transfer

import com.squareup.moshi.Json

/**
 * Data class who represent a Transfer Response data from the server.
 *
 * @param result the reply from the server if the transfer is successful or not.
 */
data class TransferResponse( @Json(name = "result") val result: Boolean)