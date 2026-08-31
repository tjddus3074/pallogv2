package com.mackereldev.pallogv2.ui.ingredientDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun IngredientDetailScreen(
    href: String,
    onBack: () -> Unit,
    viewModel: IngredientDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(href) { viewModel.loadIngredient(href) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    IngredientDetailView(uiState = uiState, onBack = onBack)
}