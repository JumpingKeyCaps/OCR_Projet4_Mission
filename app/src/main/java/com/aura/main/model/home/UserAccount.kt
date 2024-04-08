package com.aura.main.model.home


/**
 * Data class who represent a user account data.
 *
 * @param id  The id of the account.
 * @param main  to know if its the main account of the user.
 * @param balance The quantity of money on the account.
 */
data class UserAccount(val id: String, val main: Boolean, val balance: Double)