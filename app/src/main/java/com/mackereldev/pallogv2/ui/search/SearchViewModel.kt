package com.mackereldev.pallogv2.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.repository.SearchRepository
import com.mackereldev.pallogv2.data.repository.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchMode { NAME, PAL_FILTER }

data class SuitabilityFilter(val workType: String, val minLevel: Int)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _mode = MutableStateFlow(SearchMode.NAME)
    val mode: StateFlow<SearchMode> = _mode.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results.asStateFlow()

    private val _suitabilityFilters = MutableStateFlow<List<SuitabilityFilter>>(emptyList())
    val suitabilityFilters: StateFlow<List<SuitabilityFilter>> = _suitabilityFilters.asStateFlow()

    private val _selectedTypes = MutableStateFlow<Set<String>>(emptySet())
    val selectedTypes: StateFlow<Set<String>> = _selectedTypes.asStateFlow()

    private val _filteredPals = MutableStateFlow<List<Pal>>(emptyList())
    val filteredPals: StateFlow<List<Pal>> = _filteredPals.asStateFlow()

    fun selectMode(mode: SearchMode) { _mode.value = mode }

    fun onQueryChange(value: String) {
        _query.value = value
        viewModelScope.launch(Dispatchers.IO) {
            val current = value
            val result = repository.search(current)
            if (_query.value == current) _results.value = result
        }
    }

    fun addSuitabilityFilter(workType: String) {
        if(_suitabilityFilters.value.any { it.workType == workType }) return
        _suitabilityFilters.value = _suitabilityFilters.value + SuitabilityFilter(workType, minLevel = 1)
        runPalFilter()
    }

    fun updateSuitabilityLevel(workType: String, minLevel: Int) {
        _suitabilityFilters.value = _suitabilityFilters.value.map {
            if(it.workType == workType) it.copy(minLevel = minLevel) else it
        }
        runPalFilter()
    }

    fun removeSuitabilityFilter(workType: String) {
        _suitabilityFilters.value = _suitabilityFilters.value.filterNot { it.workType == workType }
        runPalFilter()
    }

    fun toggleType(type: String) {
        _selectedTypes.value = if (type in _selectedTypes.value) {
            _selectedTypes.value - type
        } else{
            _selectedTypes.value + type
        }
        runPalFilter()
    }

    private fun runPalFilter() {
        viewModelScope.launch(Dispatchers.IO) {
            val minMap = _suitabilityFilters.value.associate { it.workType to it.minLevel }
            _filteredPals.value = repository.filterPals(minMap, _selectedTypes.value)
        }
    }
}