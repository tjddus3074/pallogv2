package com.mackereldev.pallogv2.ui.ItemList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ItemListScreen(
    onMenuClick: () -> Unit,
    viewModel: ItemListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    ItemListView(
        uiState = uiState,
        selectedCategory = selectedCategory,
        onCategorySelect = { viewModel.selectCategory(it) },
        onMenuClick = onMenuClick
    )
}