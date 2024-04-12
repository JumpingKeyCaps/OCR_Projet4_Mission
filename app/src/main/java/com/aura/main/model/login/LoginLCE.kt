package com.aura.main.model.login

/**
 * LCE for the login activity
 *
 *  Login Current Error pattern to Defines the different states of the login activity.
 *
 *  - LoginLoading : the state when the activity is loading some data.
 *  - LoginSuccess : the state when the activity as successfully load data.
 *  - LoginError : the state when the activity as an error to load data.
 */
sealed class LoginLCE {
    data class LoginLoading(val loadingMessage: Int) : LoginLCE()
    data class LoginCurrent(val fieldIsOK: Boolean, val granted: Boolean) : LoginLCE()
    data class LoginError(val errorMessage: Int) : LoginLCE()
}