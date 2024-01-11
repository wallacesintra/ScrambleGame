package com.example.scramblegame

data class ScrambleUiState(
    val progress: Float = 0.0f,
    val score: Int = 0,
    val currentWord: String = "",
    val userGuess: String = "",
    val wrongGuess: Boolean = false,
    val gameOver: Boolean = false
)
