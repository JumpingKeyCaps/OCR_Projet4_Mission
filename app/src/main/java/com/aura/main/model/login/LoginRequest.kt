package com.aura.main.model.login

import com.squareup.moshi.Json

/**
 * Data class who represent a LoginRequest data.
 *
 * @param id  The user identifier to login.
 * @param password  The user password to login.
 */

data class LoginRequest(@Json(name = "id") val id: String, @Json(name = "password") val password: String)