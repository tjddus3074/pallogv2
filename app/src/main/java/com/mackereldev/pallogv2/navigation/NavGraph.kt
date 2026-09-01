package com.mackereldev.pallogv2.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.ui.ItemList.ItemListScreen
import com.mackereldev.pallogv2.ui.accessoryDetail.AccessoryDetailScreen
import com.mackereldev.pallogv2.ui.ammoDetail.AmmoDetailScreen
import com.mackereldev.pallogv2.ui.armorDetail.ArmorDetailScreen
import com.mackereldev.pallogv2.ui.buildingList.BuildingListScreen
import com.mackereldev.pallogv2.ui.buildingSuitabilityDetail.BuildingSuitabilityDetailScreen
import com.mackereldev.pallogv2.ui.consumableDetail.ConsumableDetailScreen
import com.mackereldev.pallogv2.ui.ingredientDetail.IngredientDetailScreen
import com.mackereldev.pallogv2.ui.keyItemDetail.KeyItemDetailScreen
import com.mackereldev.pallogv2.ui.materialDetail.MaterialDetailScreen
import com.mackereldev.pallogv2.ui.palDetail.PalDetailScreen
import com.mackereldev.pallogv2.ui.palList.PalListScreen
import com.mackereldev.pallogv2.ui.productionDetail.ProductionDetailScreen
import com.mackereldev.pallogv2.ui.schematicsDetail.SchematicsDetailScreen
import com.mackereldev.pallogv2.ui.sphereDetail.SphereDetailScreen
import com.mackereldev.pallogv2.ui.sphereModuleDetail.SphereModuleDetailScreen
import com.mackereldev.pallogv2.ui.storageDetail.StorageDetailScreen
import com.mackereldev.pallogv2.ui.weaponDetail.WeaponDetailScreen


private fun ItemCategory.detailRoute(href: String): String = when (this) {
    ItemCategory.WEAPON -> "weaponDetail/${Uri.encode(href)}"
    ItemCategory.AMMO -> "ammoDetail/${Uri.encode(href)}"
    ItemCategory.ARMOR -> "armorDetail/${Uri.encode(href)}"
    ItemCategory.SPHERE -> "sphereDetail/${Uri.encode(href)}"
    ItemCategory.SPHERE_MODULE -> "sphereModuleDetail/${Uri.encode(href)}"
    ItemCategory.ACCESSORY -> "accessoryDetail/${Uri.encode(href)}"
    ItemCategory.MATERIAL -> "materialDetail/${Uri.encode(href)}"
    ItemCategory.CONSUMABLE -> "consumableDetail/${Uri.encode(href)}"
    ItemCategory.INGREDIENT -> "ingredientDetail/${Uri.encode(href)}"
    ItemCategory.KEY_ITEM -> "keyItemDetail/${Uri.encode(href)}"
}

// 생산 시설은 소속 카테고리에 따라 상세화면 라우트 형태가 다름
private fun BuildingCategory.facilityDetailRoute(href: String): String = when (this) {
    BuildingCategory.PRODUCTION -> "productionDetail/${Uri.encode(href)}"
    BuildingCategory.STORAGE -> "storageDetail/${Uri.encode(href)}"
    BuildingCategory.PAL,
    BuildingCategory.FOOD,
    BuildingCategory.INFRA,
    BuildingCategory.LIGHTING,
    BuildingCategory.FOUNDATION,
    BuildingCategory.DEFENSES,
    BuildingCategory.OTHER,
    BuildingCategory.FURNITURE,
    BuildingCategory.SCHEMATICS -> "buildingSuitabilityDetail/${this.name}/${Uri.encode(href)}"
}

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
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) }
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
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "ammoDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            AmmoDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "armorDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            ArmorDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "sphereDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            SphereDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "sphereModuleDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            SphereModuleDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "accessoryDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            AccessoryDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "materialDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            MaterialDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "consumableDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            ConsumableDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "ingredientDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            IngredientDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable(
            route = "keyItemDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            KeyItemDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) },
                onProductionClick = { category, href -> navController.navigate(category.facilityDetailRoute(href)) }
            )
        }
        composable("buildingList") {
            BuildingListScreen(
                onMenuClick = onMenuClick,
                onProductionClick = { href -> navController.navigate("productionDetail/${Uri.encode(href)}") },
                onBuildingSuitabilityClick = { category, href ->
                    navController.navigate("buildingSuitabilityDetail/${category.name}/${Uri.encode(href)}")
                },
                onStorageClick = { href -> navController.navigate("storageDetail/${Uri.encode(href)}") },
                onSchematicsClick = { href -> navController.navigate("schematicsDetail/${Uri.encode(href)}") }
            )
        }
        composable(
            route = "productionDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            ProductionDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) }
            )
        }
        composable(
            route = "buildingSuitabilityDetail/{category}/{href}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("href") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            BuildingSuitabilityDetailScreen(
                category = BuildingCategory.valueOf(backStackEntry.arguments?.getString("category") ?: BuildingCategory.PAL.name),
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) }
            )
        }
        composable(
            route = "storageDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            StorageDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) }
            )
        }
        composable(
            route = "schematicsDetail/{href}",
            arguments = listOf(navArgument("href") { type = NavType.StringType })
        ) { backStackEntry ->
            SchematicsDetailScreen(
                href = backStackEntry.arguments?.getString("href") ?: "",
                onBack = { navController.popBackStack() },
                onItemClick = { category, href -> navController.navigate(category.detailRoute(href)) }
            )
        }
    }
}