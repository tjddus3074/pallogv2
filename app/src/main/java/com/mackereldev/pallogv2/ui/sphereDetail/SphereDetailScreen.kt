package com.mackereldev.pallogv2.ui.sphereDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SphereDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: SphereDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadSphere(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SphereDetailView(uiState = uiState, onBack = onBack)
}