package com.example.qasystemappcompose.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.qasystemappcompose.viewmodel.MainViewModel


@Composable
fun MainScreen(viewModel: MainViewModel, onSubmit: () -> Unit) {
    val question by viewModel.currentQuestion.collectAsState()

    question?.let { q ->
        Column(Modifier.padding(16.dp)) {
            Text(q.question, style = MaterialTheme.typography.h6)
            var inputText by remember { mutableStateOf("") }
            val selected = remember { mutableStateListOf<String>() }

            Spacer(Modifier.height(16.dp))
            when (q.type) {
                "textInput", "numberInput" -> {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        keyboardOptions = if(q.type == "numberInput")
                            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        else KeyboardOptions.Default
                    )
                }
                "checkbox" -> {
                    q.options?.forEach { opt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selected.contains(opt),
                                onCheckedChange = { sel ->
                                    if (sel) selected.add(opt) else selected.remove(opt)
                                }
                            )
                            Text(opt)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row {
                if (q.skip != "-1") {
                    Button(onClick = { viewModel.skip() }) { Text("Skip") }
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    val ans = if (q.type=="checkbox") selected.joinToString(", ") else inputText
                    viewModel.answered(ans)
                    if (q.referTo == "submit") onSubmit()
                }) { Text(if (q.referTo == "submit") "Submit" else "Next") }
            }
        }
    }
}