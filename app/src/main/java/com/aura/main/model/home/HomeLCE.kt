package com.aura.main.model.home

/**
 * LCE for the home activity
 *
 *  Login Current Error pattern to Defines the different states of the home activity.
 *
 *  - HomeLoading : the state when the activity is loading user account.
 *  - HomeContent : the state when the activity as successfully load the account.
 *  - HomeError : the state when the activity as an error to load the account.
 */
sealed class HomeLCE {
    data class HomeLoading(val loadingMessage: Int) : HomeLCE()
    data class HomeContent(val userBalance: Double) : HomeLCE()
    data class HomeError(val errorMessage: Int) : HomeLCE()
}