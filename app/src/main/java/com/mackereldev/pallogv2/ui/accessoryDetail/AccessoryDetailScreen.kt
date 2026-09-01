package com.mackereldev.pallogv2.ui.accessoryDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory

@Composable
fun AccessoryDetailScreen(
    href: String,
    onBack: ()-> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit,
    viewModel: AccessoryDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadAccessory(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AccessoryDetailView(
        uiState = uiState,
        onBack = onBack,
        onItemClick = onItemClick,
        onProductionClick = onProductionClick
    )
}
