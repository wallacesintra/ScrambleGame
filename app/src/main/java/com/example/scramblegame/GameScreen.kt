package com.example.scramblegame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scramblegame.ui.theme.ScrambleGameTheme


@Composable
fun GameLayout(
    scrambleViewModel: ScrambleViewModel = viewModel()
){
    val scrambleUiState by scrambleViewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .padding(10.dp)
        )
        GameMainLayout(
            modifier = Modifier,
            text = scrambleViewModel.useGuess,
            currentWord = scrambleUiState.currentWord,
            keyBoardDone = { scrambleViewModel.checkUserGuess() },
            userGuess = scrambleViewModel.useGuess,
            onValueChange = { scrambleViewModel.updateUserGuess(it) },
            wrongGuess = scrambleUiState.wrongGuess,
            progress = scrambleUiState.progress
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { scrambleViewModel.skip() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.skip),
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ScoreDisplay(score = scrambleUiState.score)
        if (scrambleUiState.gameOver){
            FinalScore(
                score = scrambleUiState.score,
                playAgain = { scrambleViewModel.resetScramble() }
            )
        }
    }
}

@Composable
fun GameMainLayout(
    modifier: Modifier,
    currentWord: String,
    text: String,
    keyBoardDone: ()-> Unit,
    onValueChange: (String) -> Unit,
    userGuess: String,
    wrongGuess: Boolean,
    progress: Float

){
    var txtInput by remember{ mutableStateOf(text)}
    Card(
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)

        ){
            Progress(
                progress = progress,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
            Text(
                text = stringResource(id = R.string.word_to_unscramble, currentWord),
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                label = {
                    if (wrongGuess){
                        Text(text = stringResource(id = R.string.try_again))
                    }
                    else(
                        Text(text = stringResource(id = R.string.enter_text))
                    )

                },
                value = userGuess,
                onValueChange =  onValueChange ,
                isError = wrongGuess,
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { keyBoardDone() }
                )
            )



        }
    }
}

@Composable
fun ScoreDisplay(
    score: Int
){
    Text(
        text = stringResource(id = R.string.score, score),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(10.dp)
    )
}

@Composable
fun FinalScore(
    modifier: Modifier = Modifier,
    score: Int,
    playAgain: () -> Unit
){
    AlertDialog(
        onDismissRequest = { /*TODO*/ },

        title = {
            if (score > 60){
                Text(
                    text = stringResource(id = R.string.congratulation),
                )
            }
            else{
                Text(text = stringResource(id = R.string.nice_try))
            }

        },
        text = {
            Text(text = stringResource(id = R.string.have_scored, score))
        },
//        confirmButton = { playAgain() },
        confirmButton = {
            TextButton(onClick = { playAgain()}) {
                Text(text = stringResource(id = R.string.play_again))
                
            }
        }


    )

}

@Composable
fun Progress(
    progress: Float,
    modifier: Modifier = Modifier
){
    CircularProgressIndicator(
        progress = progress,
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .padding(10.dp),
    )

}

@Preview(showBackground = true)
@Composable
fun ScreenPreview(){
    ScrambleGameTheme {
        FinalScore(
            score = 100,
            playAgain = {}
        )
    }
}