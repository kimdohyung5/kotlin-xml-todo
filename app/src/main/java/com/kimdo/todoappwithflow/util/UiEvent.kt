package com.kimdo.todoappwithflow.util

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val id: Int): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()

    data class ValueChange( val title:String, val description:String): UiEvent()
}