package com.aura.main.model


/**
 * Screen State Class -
 * a sealed class with generic type T
 * to specify the type of data the Content state holds.
 */
sealed class ScreenState<out T> {
    data class Loading(val message: Int) : ScreenState<Nothing>()
    data class Error(val message: Int) : ScreenState<Nothing>()
    class Content<out T : Any>(val data: T) : ScreenState<T>()
}