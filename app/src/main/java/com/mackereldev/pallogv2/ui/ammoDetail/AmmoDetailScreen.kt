package com.mackereldev.pallogv2.ui.ammoDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory

@Composable
fun AmmoDetailScreen(
    href: String,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit,
    viewModel: AmmoDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadAmmo(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AmmoDetailView(
        uiState = uiState,
        onBack = onBack,
        onItemClick = onItemClick,
        onProductionClick = onProductionClick
    )
}
