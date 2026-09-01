package com.mackereldev.pallogv2.ui.consumableDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory

@Composable
fun ConsumableDetailScreen(
    href: String,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit,
    viewModel: ConsumableDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadConsumable(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ConsumableDetailView(
        uiState = uiState,
        onBack = onBack,
        onItemClick = onItemClick,
        onProductionClick = onProductionClick
    )
}
