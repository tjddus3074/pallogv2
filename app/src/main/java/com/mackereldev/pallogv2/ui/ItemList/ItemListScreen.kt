package com.mackereldev.pallogv2.ui.ItemList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ItemListScreen(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onWeaponClick: (String) -> Unit,
    onAmmoClick: (String) -> Unit,
    onArmorClick: (String) -> Unit,
    onSphereClick: (String) -> Unit,
    onSphereModuleClick: (String) -> Unit,
    onAccessoryClick: (String) -> Unit,
    onMaterialClick: (String) -> Unit,
    onConsumableClick: (String) -> Unit,
    onIngredientClick: (String) -> Unit,
    onKeyItemClick: (String) -> Unit,
    viewModel: ItemListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    ItemListView(
        uiState = uiState,
        selectedCategory = selectedCategory,
        onCategorySelect = { viewModel.selectCategory(it) },
        onMenuClick = onMenuClick,
        onSearchClick = onSearchClick,
        onWeaponClick = onWeaponClick,
        onAmmoClick = onAmmoClick,
        onArmorClick = onArmorClick,
        onSphereClick = onSphereClick,
        onSphereModuleClick = onSphereModuleClick,
        onAccessoryClick = onAccessoryClick,
        onMaterialClick = onMaterialClick,
        onConsumableClick = onConsumableClick,
        onIngredientClick = onIngredientClick,
        onKeyItemClick = onKeyItemClick
    )
}