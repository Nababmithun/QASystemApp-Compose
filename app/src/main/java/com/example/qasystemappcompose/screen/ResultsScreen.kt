package com.example.qasystemappcompose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.qasystemappcompose.database.QuestionAnswer
import com.example.qasystemappcompose.viewmodel.MainViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*         // ✅ Only Material3
import androidx.compose.ui.Modifier

@Composable
fun ResultsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var answers by remember { mutableStateOf<List<QuestionAnswer>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel.getAllAnswers { answers = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Submitted Answers",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(answers) { qa ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = viewModel.getQuestionTextById(qa.questionId),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Answer: ${qa.answer}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Divider(modifier = Modifier.padding(vertical = 6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restart Survey")
        }
    }
}