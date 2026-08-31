package com.mackereldev.pallogv2.ui.keyItemDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun KeyItemDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: KeyItemDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadKeyItem(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    KeyItemDetailView(uiState = uiState, onBack = onBack)
}