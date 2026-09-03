package com.mackereldev.pallogv2.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.data.repository.SearchNavTarget

@Composable
fun SearchScreen(
    onMenuClick: () -> Unit,
    onResultClick: (SearchNavTarget) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val mode by viewModel.mode.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val results by viewModel.results.collectAsStateWithLifecycle()
    val suitabilityFilters by viewModel.suitabilityFilters.collectAsStateWithLifecycle()
    val selectedTypes by viewModel.selectedTypes.collectAsStateWithLifecycle()
    val filteredPals by viewModel.filteredPals.collectAsStateWithLifecycle()

    SearchView(
        mode = mode,
        query = query,
        results = results,
        suitabilityFilters = suitabilityFilters,
        selectedTypes = selectedTypes,
        filteredPals = filteredPals,
        onMenuClick = onMenuClick,
        onModeSelect = { viewModel.selectMode(it) },
        onQueryChange = { viewModel.onQueryChange(it) },
        onAddSuitabilityFilter = { viewModel.addSuitabilityFilter(it) },
        onSuitabilityLevelChange = { work, level -> viewModel.updateSuitabilityLevel(work, level) },
        onRemoveSuitabilityFilter = { viewModel.removeSuitabilityFilter(it) },
        onToggleType = { viewModel.toggleType(it) },
        onResultClick = { onResultClick(it.target) }
    )
}