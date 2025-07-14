package com.example.qasystemappcompose.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qasystemappcompose.viewmodel.MainViewModel


@Composable
fun MainScreen(viewModel: MainViewModel, onCompleted: () -> Unit) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()

    currentQuestion?.let { question ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = question.question.slug, fontSize = 20.sp)

            question.options?.forEach { option ->
                Button(
                    onClick = { viewModel.answered(option.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(option.value)
                }
            }

            Button(
                onClick = { viewModel.skip() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Skip")
            }
        }
    } ?: run {
        // ✅ Navigate to result screen
        LaunchedEffect(Unit) {
            onCompleted()
        }
        Text(
            "Thanks! You've completed the flow.",
            modifier = Modifier.padding(16.dp),
            fontSize = 22.sp
        )
    }
}