package com.mackereldev.pallogv2.ui.weaponDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WeaponDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: WeaponDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadWeapon(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WeaponDetailView(uiState = uiState, onBack = onBack)
}