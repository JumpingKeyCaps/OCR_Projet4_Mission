package com.aura.main.model.login

/**
 * Data class who represent a LoginRequest data.
 *
 * @param id  The user identifier to login.
 * @param password  The user password to login.
 */
data class LoginRequest(val id: String, val password: String)