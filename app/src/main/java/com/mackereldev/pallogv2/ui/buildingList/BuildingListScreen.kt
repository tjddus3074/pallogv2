package com.mackereldev.pallogv2.ui.buildingList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.BuildingCategory

@Composable
fun BuildingListScreen(
    onMenuClick: () -> Unit,
    onProductionClick: (String) -> Unit,
    onBuildingSuitabilityClick: (BuildingCategory, String) -> Unit,
    onStorageClick: (String) -> Unit,
    onSchematicsClick: (String) -> Unit,
    viewModel: BuildingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    BuildingListView(
        uiState = uiState,
        selectedCategory = selectedCategory,
        onCategorySelect = { viewModel.selectCategory(it) },
        onMenuClick = onMenuClick,
        onProductionClick = onProductionClick,
        onBuildingSuitabilityClick = onBuildingSuitabilityClick,
        onStorageClick = onStorageClick,
        onSchematicsClick = onSchematicsClick
    )
}