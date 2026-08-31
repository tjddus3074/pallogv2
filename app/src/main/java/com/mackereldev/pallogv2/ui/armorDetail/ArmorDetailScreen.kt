package com.mackereldev.pallogv2.ui.armorDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ArmorDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: ArmorDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadArmor(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArmorDetailView(uiState = uiState, onBack = onBack)
}
