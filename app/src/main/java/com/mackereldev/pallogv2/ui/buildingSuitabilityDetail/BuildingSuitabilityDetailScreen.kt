package com.mackereldev.pallogv2.ui.buildingSuitabilityDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory

@Composable
fun BuildingSuitabilityDetailScreen(
    category: BuildingCategory,
    href: String,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    viewModel: BuildingSuitabilityDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(category, href) { viewModel.loadBuilding(category, href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BuildingSuitabilityDetailView(uiState = uiState, onBack = onBack, onItemClick = onItemClick)
}