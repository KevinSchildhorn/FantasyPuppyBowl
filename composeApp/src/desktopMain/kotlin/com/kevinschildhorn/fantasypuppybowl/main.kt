package com.kevinschildhorn.fantasypuppybowl

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FantasyPuppyBowl",
    ) {
        App()
    }
}