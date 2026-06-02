package com.mackereldev.pallogv2.ui.buildingList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BuildingListScreen(
    onMenuClick: () -> Unit,
    viewModel: BuildingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    BuildingListView(
        uiState = uiState,
        selectedCategory = selectedCategory,
        onCategorySelect = { viewModel.selectCategory(it) },
        onMenuClick = onMenuClick
    )
}