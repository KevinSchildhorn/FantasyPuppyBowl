package com.kevinschildhorn.fantasypuppybowl

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val dogViewModel = DogViewModel()
    MaterialTheme {
        DogView(dogViewModel)
    }
}