package com.aura.main.model.login

/**
 * Login content screen state
 *
 * @param fieldIsOK all the screen text fields are completed.
 * @param granted the login is successful.
 */
data class LoginContent(val fieldIsOK: Boolean, val granted: Boolean)