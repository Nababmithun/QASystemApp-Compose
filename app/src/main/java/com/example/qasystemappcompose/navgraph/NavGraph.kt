package com.example.qasystemappcompose.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.qasystemappcompose.screen.MainScreen
import com.example.qasystemappcompose.screen.ResultsScreen
import com.example.qasystemappcompose.viewmodel.MainViewModel

@Composable
fun NavGraph(vm: MainViewModel, onSubmitDone: () -> Unit) {
    val nav = rememberNavController()
    NavHost(nav, startDestination = "survey") {
        composable("survey") {
            MainScreen(viewModel = vm) {
                nav.navigate("results")
            }
        }
        composable("results") {
            ResultsScreen(viewModel = vm, onBack = {
                nav.popBackStack("survey", inclusive = true)
                vm.fetchAll()
            })
        }
    }
}