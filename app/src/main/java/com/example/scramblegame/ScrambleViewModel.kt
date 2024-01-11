package com.example.scramblegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scramblegame.data.words
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScrambleViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ScrambleUiState())
    val uiState: StateFlow<ScrambleUiState> = _uiState.asStateFlow()
    var useGuess by mutableStateOf("")
        private set

    private lateinit var currentWord: String

    private var usedWords = mutableSetOf<String>()

    private fun pickRandomWord(): String{
        currentWord = words.random()
        if (usedWords.contains(currentWord)){
            pickRandomWord()
        }
        usedWords.add(currentWord)
        return shuffleWord(currentWord)
    }



    private fun shuffleWord(word: String): String{
        val temp = word.toCharArray()
        temp.shuffle()
        if (String(temp) == word) {
            temp.shuffle()
        }
        return String(temp)
    }

    fun updateUserGuess(guessedWord: String){
        useGuess = guessedWord
    }

    private fun updateProgress(wordCount: Int): Float {
        return (wordCount.toFloat() * 1.0f) / 10
    }
    private fun updateGameUi(updatedScore: Int){
        if (usedWords.size == 10){
            _uiState.update {
                currentState -> currentState.copy(
                    wrongGuess = false,
                    score = updatedScore,
                    progress = updateProgress(usedWords.size),
                    gameOver = true
                )
            }
        }
        else{
            _uiState.update {
                currentState -> currentState.copy(
                    currentWord = pickRandomWord(),
                    wrongGuess = false,
                    score = updatedScore,
                    progress = updateProgress(usedWords.size)

                )
            }
        }
    }
    fun checkUserGuess(){
        if (useGuess == currentWord){
            val updateScore = _uiState.value.score.plus(10)
            updateGameUi(updateScore)
            updateUserGuess("")
        }
        else{
            _uiState.update { currentState -> currentState.copy(
                wrongGuess = true
            ) }
        }
    }
    fun skip(){
        updateGameUi(_uiState.value.score)
        updateUserGuess("")
    }

    fun resetScramble(){
        usedWords.clear()
        _uiState.value = ScrambleUiState(currentWord = pickRandomWord())
    }
    init {
        resetScramble()
    }
}