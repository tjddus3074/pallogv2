package com.mackereldev.pallogv2.ui.consumableDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ConsumableDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: ConsumableDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadConsumable(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ConsumableDetailView(uiState = uiState, onBack = onBack)
}