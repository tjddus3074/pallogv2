package com.mackereldev.pallogv2.ui.buildingSuitabilityDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.Building
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.repository.BuildingRepository
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BuildingSuitabilityDetailUiState {
    object Loading : BuildingSuitabilityDetailUiState()
    data class Success(
        val building: Building,
        val category: BuildingCategory,
        val mateiralIcons: Map<String, String>,
        val materialLocations: Map<String, Pair<ItemCategory, String>>
    ) : BuildingSuitabilityDetailUiState()
    data class Error(val message: String) : BuildingSuitabilityDetailUiState()
}

@HiltViewModel
class BuildingSuitabilityDetailViewModel @Inject constructor(
    private val buildingRepository: BuildingRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BuildingSuitabilityDetailUiState>(BuildingSuitabilityDetailUiState.Loading)
    val uiState: StateFlow<BuildingSuitabilityDetailUiState> = _uiState.asStateFlow()

    fun loadBuilding(category: BuildingCategory, href: String) {
        _uiState.value = BuildingSuitabilityDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { buildingRepository.getBuilding(category, href) }
                .onSuccess { building ->
                    if(building == null) {
                        _uiState.value = BuildingSuitabilityDetailUiState.Error("건축물을 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialIcons = building.material.keys.distinct().mapNotNull { name ->
                        itemRepository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    val materialLocations = building.material.keys.distinct().mapNotNull { name ->
                        itemRepository.findItemLocation(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = BuildingSuitabilityDetailUiState.Success(building, category, materialIcons, materialLocations)
                }
                .onFailure { _uiState.value = BuildingSuitabilityDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}