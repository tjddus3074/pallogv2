package com.mackereldev.pallogv2.ui.storageDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.ItemCategory

@Composable
fun StorageDetailScreen(
    href: String,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    viewModel: StorageDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadBuilding(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StorageDetailView(uiState = uiState, onBack = onBack, onItemClick = onItemClick)
}