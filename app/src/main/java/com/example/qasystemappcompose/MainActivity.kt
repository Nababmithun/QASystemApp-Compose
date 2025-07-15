package com.example.qasystemappcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qasystemappcompose.database.AppDatabase
import com.example.qasystemappcompose.navgraph.NavGraph
import com.example.qasystemappcompose.screen.MainScreen
import com.example.qasystemappcompose.viewmodel.MainViewModel
import com.example.qasystemappcompose.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(this)
        val viewModel = MainViewModel(db)

        setContent {
            MaterialTheme {
                NavGraph(
                    vm = viewModel,
                    onSubmitDone = {
                        // 👉 Restart Logic  ResultsScreen  Start Over
                        viewModel.restartSurvey()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                )
            }
        }
    }
}