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
    onRestart: () -> Unit
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
            text = "📝 Submitted Answers",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(answers) { qa ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = viewModel.getQuestionTextById(qa.questionId),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Answer: ${qa.answer}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.restartSurvey()
                onRestart()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("🔁 Restart Survey", style = MaterialTheme.typography.titleMedium)
        }
    }
}