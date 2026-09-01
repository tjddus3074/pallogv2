package com.mackereldev.pallogv2.ui.schematicsDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.model.ItemCategory

@Composable
fun SchematicsDetailScreen(
    href: String,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    viewModel: SchematicsDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadSchematics(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SchematicsDetailView(uiState = uiState, onBack = onBack, onItemClick = onItemClick)
}