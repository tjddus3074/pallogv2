package com.mackereldev.pallogv2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mackereldev.pallogv2.ui.palDetail.PalDetailScreen
import com.mackereldev.pallogv2.ui.palList.PalListScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "palList") {
        composable("palList") {
            PalListScreen(
                onPalClick = { deckIndex ->
                    navController.navigate("palDetail/$deckIndex")
                }
            )
        }
        composable(
            route = "palDetail/{deckIndex}",
            arguments = listOf(navArgument("deckIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            PalDetailScreen(
                deckIndex = backStackEntry.arguments?.getInt("deckIndex") ?: 1,
                onBack = { navController.popBackStack() }
            )
        }
    }
}