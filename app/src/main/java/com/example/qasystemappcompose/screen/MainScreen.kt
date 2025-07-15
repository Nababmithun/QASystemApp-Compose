package com.example.qasystemappcompose.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qasystemappcompose.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onCompleted: () -> Unit
) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val questionIndex by viewModel.currentQuestionIndex.collectAsState()

    currentQuestion?.let { question ->
        key(questionIndex) {
            var selectedOption by remember(questionIndex) { mutableStateOf<String?>(null) }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = question.question.slug, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Show options if available
                question.options?.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedOption = option.value }
                            .background(
                                if (selectedOption == option.value)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == option.value,
                            onClick = { selectedOption = option.value }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option.value)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // ✅ Show Skip button only if skip.id != "-1"
                    if (question.skip?.id != "-1") {
                        Button(
                            onClick = { viewModel.skip() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Skip")
                        }
                    }

                    // ✅ If referTo is 'submit', show Submit button
                    if (question.referTo?.id == "submit") {
                        Button(
                            onClick = {
                                selectedOption?.let { viewModel.answered(it) }
                            },
                            enabled = selectedOption != null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Submit")
                        }
                    } else {
                        // ✅ Show Next button for other question types
                        Button(
                            onClick = {
                                selectedOption?.let { viewModel.answered(it) }
                            },
                            enabled = selectedOption != null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    } ?: run {
        // No more questions: navigate to results
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