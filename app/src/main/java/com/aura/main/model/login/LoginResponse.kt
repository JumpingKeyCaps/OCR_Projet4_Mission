package com.aura.main.model.login

import com.squareup.moshi.Json

/**
 * Data class who represent a LoginResponse data from the server.
 *
 * @param granted  the reply from the server if the connexion is granted or not.
 */
data class LoginResponse(@Json(name = "granted") val granted: Boolean)