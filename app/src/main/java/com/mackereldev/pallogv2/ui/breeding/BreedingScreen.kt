package com.mackereldev.pallogv2.ui.breeding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BreedingScreen(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPalClick: (String) -> Unit,
    viewModel: BreedingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mode by viewModel.mode.collectAsStateWithLifecycle()
    val parent1 by viewModel.parent1.collectAsStateWithLifecycle()
    val parent2 by viewModel.parent2.collectAsStateWithLifecycle()
    val child by viewModel.child.collectAsStateWithLifecycle()
    val material by viewModel.material.collectAsStateWithLifecycle()
    val comboFilter by viewModel.comboFilter.collectAsStateWithLifecycle()
    val childResults by viewModel.childResults.collectAsStateWithLifecycle()
    val resultCombos by viewModel.resultCombos.collectAsStateWithLifecycle()
    val parentPairs by viewModel.parentPairs.collectAsStateWithLifecycle()

    BreedingView(
        uiState = uiState,
        mode = mode,
        parent1 = parent1,
        parent2 = parent2,
        child = child,
        material = material,
        comboFilter = comboFilter,
        childResults = childResults,
        resultCombos = resultCombos,
        parentPairs = parentPairs,
        combos = viewModel.filteredCombos(),
        onMenuClick = onMenuClick,
        onSearchClick = onSearchClick,
        onModeSelect = { viewModel.selectMode(it) },
        onParent1Select = { viewModel.selectParent1(it) },
        onParent2Select = { viewModel.selectParent2(it) },
        onChildSelect = { viewModel.selectChild(it) },
        onMaterialSelect = { viewModel.selectMaterial(it) },
        onComboFilterSelect = { viewModel.selectComboFilter(it) },
        onPalClick = { pal -> onPalClick(pal.stats.code) }
    )
}
