package com.mackereldev.pallogv2.ui.palDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PalDetailScreen(
    deckIndex: Int,
    onBack: () -> Unit,
    viewModel: PalDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(deckIndex) { viewModel.loadPal(deckIndex) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PalDetailView(uiState = uiState, onBack = onBack)

}