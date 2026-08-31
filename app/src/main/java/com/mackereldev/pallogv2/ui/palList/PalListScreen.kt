package com.mackereldev.pallogv2.ui.palList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PalListScreen(
    onPalClick: (String) -> Unit,
    onMenuClick: () -> Unit,
    viewModel: PalListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PalListView(uiState = uiState, onPalClick = onPalClick, onMenuClick = onMenuClick)

}