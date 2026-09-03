package com.mackereldev.pallogv2.ui.tech

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.repository.TechNavTarget

@Composable
fun TechScreen(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNavigate: (TechNavTarget) -> Unit,
    viewModel: TechViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TechView(
        uiState = uiState,
        onMenuClick = onMenuClick,
        onSearchClick = onSearchClick,
        onItemClick = { itemName -> viewModel.resolveTarget(itemName)?.let(onNavigate) }
    )
}