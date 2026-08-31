package com.mackereldev.pallogv2.ui.ammoDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AmmoDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: AmmoDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadAmmo(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AmmoDetailView(uiState = uiState, onBack = onBack)
}