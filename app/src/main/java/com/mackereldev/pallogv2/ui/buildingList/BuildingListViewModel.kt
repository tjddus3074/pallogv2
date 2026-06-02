package com.mackereldev.pallogv2.ui.buildingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.Building
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.repository.BuildingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BuildingListUiState {
    object Loading : BuildingListUiState()
    data class Success(val buildings: List<Building>) : BuildingListUiState()
    data class Error(val message: String) : BuildingListUiState()
}

@HiltViewModel
class BuildingListViewModel @Inject constructor(
    private val repository: BuildingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BuildingListUiState>(BuildingListUiState.Loading)
    val uiState: StateFlow<BuildingListUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow(BuildingCategory.PRODUCTION)
    val selectedCategory: StateFlow<BuildingCategory> = _selectedCategory.asStateFlow()

    init { loadBuildings(BuildingCategory.PRODUCTION) }

    fun selectCategory(category: BuildingCategory) {
        _selectedCategory.value = category
        loadBuildings(category)
    }

    private fun loadBuildings(category: BuildingCategory) {
        _uiState.value = BuildingListUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getBuildings(category) }
                .onSuccess { _uiState.value = BuildingListUiState.Success(it) }
                .onFailure { _uiState.value = BuildingListUiState.Error(it.message ?: "오류 발생") }
        }
    }
}