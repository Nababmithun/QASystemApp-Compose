package com.example.qasystemappcompose.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.qasystemappcompose.screen.MainScreen
import com.example.qasystemappcompose.screen.ResultsScreen
import com.example.qasystemappcompose.viewmodel.MainViewModel

@Composable
fun NavGraph(vm: MainViewModel, onSubmitDone: () -> Unit) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "survey") {
        composable("survey") {
            MainScreen(
                viewModel = vm,
                onCompleted = {
                    nav.navigate("results")
                }
            )
        }

        composable("results") {
            ResultsScreen(
                viewModel = vm,
                onRestart = {
                    vm.restartSurvey()
                    nav.navigate("survey") {
                        popUpTo(0) { inclusive = true } // ⛔️ Clear full backstack
                        launchSingleTop = true           // 🧼 Avoid duplicate
                    }
                }
            )
        }
    }
}