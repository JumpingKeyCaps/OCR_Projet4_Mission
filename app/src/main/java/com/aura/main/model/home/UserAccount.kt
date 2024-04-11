package com.aura.main.model.home

import com.squareup.moshi.Json


/**
 * Data class who represent a user account data.
 *
 * @param id  The id of the account.
 * @param main  to know if its the main account of the user.
 * @param balance The quantity of money on the account.
 */
data class UserAccount(
    @Json(name = "id") val id: String,
    @Json(name = "main") val main: Boolean,
    @Json(name = "balance") val balance: Double)