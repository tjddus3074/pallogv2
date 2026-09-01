package com.mackereldev.pallogv2.ui.productionDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.ItemCategory

@Composable
fun ProductionDetailScreen(
    href: String,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    viewModel: ProductionDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadProduction(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProductionDetailView(uiState = uiState, onBack = onBack, onItemClick = onItemClick)
}
