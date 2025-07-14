package com.example.qasystemappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qasystemappcompose.database.AppDatabase
import com.example.qasystemappcompose.navgraph.NavGraph
import com.example.qasystemappcompose.viewmodel.MainViewModel
import com.example.qasystemappcompose.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val factory = MainViewModelFactory(database)

        setContent {
            val viewModel: MainViewModel = viewModel(factory = factory)
            NavGraph(viewModel) { /* onSubmitDone */ }
        }
    }
}