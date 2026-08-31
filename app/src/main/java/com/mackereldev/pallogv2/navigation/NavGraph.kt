package com.mackereldev.pallogv2.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mackereldev.pallogv2.ui.ItemList.ItemListScreen
import com.mackereldev.pallogv2.ui.accessoryDetail.AccessoryDetailScreen
import com.mackereldev.pallogv2.ui.ammoDetail.AmmoDetailScreen
import com.mackereldev.pallogv2.ui.armorDetail.ArmorDetailScreen
import com.mackereldev.pallogv2.ui.buildingList.BuildingListScreen
import com.mackereldev.pallogv2.ui.consumableDetail.ConsumableDetailScreen
import com.mackereldev.pallogv2.ui.ingredientDetail.IngredientDetailScreen
import com.mackereldev.pallogv2.ui.keyItemDetail.KeyItemDetailScreen
import com.mackereldev.pallogv2.ui.materialDetail.MaterialDetailScreen
import com.mackereldev.pallogv2.ui.palDetail.PalDetailScreen
import com.mackereldev.pallogv2.ui.palList.PalListScreen
import com.mackereldev.pallogv2.ui.sphereDetail.SphereDetailScreen
import com.mackereldev.pallogv2.ui.sphereModuleDetail.SphereModuleDetailScreen
import com.mackereldev.pallogv2.ui.weaponDetail.WeaponDetailScreen


@Composable
fun NavGraph(navController: NavHostController, onMenuClick: () -> Unit) {
    NavHost(navController = navController, startDestination = "palList") {
        composable("palList") {
            PalListScreen(
                onPalClick = { code ->
                    navController.navigate("palDetail/${Uri.encode(code)}")
                },
                onMenuClick = onMenuClick
            )
        }
        composable(
            route = "palDetail/{code}",
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) { backStackEntry ->
            PalDetailScreen(
                code = backStackEntry.arguments?.getString("code") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable("itemList") {
            ItemListScreen(
                onMenuClick = onMenuClick,
                onWeaponClick = { href -> navController.navigate("weaponDetail/${Uri.encode(href)}") },
                onAmmoClick = { href -> navController.navigate("ammoDetail/${Uri.encode(href)}") },
                onArmorClick = { href -> navController.navigate("armorDetail/${Uri.encode(href)}") },
                onSphereClick = { href -> navController.navigate("sphereDetail/${Uri.encode(href)}") },
                onSphereModuleClick = { href -> navController.navigate("sphereModuleDetail/${Uri.encode(href)}") },
                onAccessoryClick = { href -> navController.navigate("accessoryDetail/${Uri.encode(href)}") },
                onMaterialClick = { href -> navController.navigate("materialDetail/${Uri.encode(href)}") },
                onConsumableClick = { href -> navController.navigate("consumableDetail/${Uri.encode(href)}") },
                onIngredientClick = { href -> navController.navigate("ingredientDetail/${Uri.encode(href)}") },
                onKeyItemClick = { href -> navController.navigate("keyItemDetail/${Uri.encode(href)}") }
            )
        }
        composable(
            route = "weaponDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            WeaponDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "ammoDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            AmmoDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "armorDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            ArmorDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "sphereDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            SphereDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "sphereModuleDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            SphereModuleDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "accessoryDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            AccessoryDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "materialDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            MaterialDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "consumableDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            ConsumableDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "ingredientDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            IngredientDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "keyItemDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            KeyItemDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable("buildingList") {
            BuildingListScreen(onMenuClick = onMenuClick)
        }
    }
}