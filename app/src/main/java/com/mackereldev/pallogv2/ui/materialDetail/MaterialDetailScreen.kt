package com.mackereldev.pallogv2.ui.materialDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MaterialDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: MaterialDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadMaterial(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MaterialDetailView(uiState = uiState, onBack = onBack)
}