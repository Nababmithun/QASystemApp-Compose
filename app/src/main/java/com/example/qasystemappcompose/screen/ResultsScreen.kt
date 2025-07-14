package com.example.qasystemappcompose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.qasystemappcompose.database.QuestionAnswer
import com.example.qasystemappcompose.viewmodel.MainViewModel

@Composable
fun ResultsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    var answers by remember { mutableStateOf<List<QuestionAnswer>>(emptyList()) }
    LaunchedEffect(Unit) { viewModel.getAllAnswers { answers = it } }

    Column(Modifier.padding(16.dp)) {
        Text("Submitted Answers", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))
        answers.forEach { qa ->
            Text("${qa.questionId}: ${qa.answer}")
            Divider()
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) { Text("Restart Survey") }
    }
}