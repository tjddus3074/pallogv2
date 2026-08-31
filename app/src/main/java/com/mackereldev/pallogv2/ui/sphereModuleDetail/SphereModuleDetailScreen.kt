package com.mackereldev.pallogv2.ui.sphereModuleDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SphereModuleDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: SphereModuleDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadModule(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SphereModuleDetailView(uiState = uiState, onBack = onBack)
}