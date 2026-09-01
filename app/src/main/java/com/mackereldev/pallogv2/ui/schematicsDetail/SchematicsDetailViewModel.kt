package com.mackereldev.pallogv2.ui.schematicsDetail

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

sealed class SchematicsDetailUiState{
    object Loading : SchematicsDetailUiState()
    data class Success(
        val building: Building,
        val materialIcons: Map<String, String>,
        val materialLocations: Map<String, Pair<ItemCategory, String>>
    ) : SchematicsDetailUiState()
    data class Error(val message: String) : SchematicsDetailUiState()
}

@HiltViewModel
class SchematicsDetailViewModel @Inject constructor(
    private val buildingRepository: BuildingRepository,
    private val ItemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SchematicsDetailUiState>(SchematicsDetailUiState.Loading)
    val uiState : StateFlow<SchematicsDetailUiState> = _uiState.asStateFlow()

    fun loadSchematics(href: String) {
        _uiState.value = SchematicsDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { buildingRepository.getBuilding(BuildingCategory.SCHEMATICS, href) }
                .onSuccess { building ->
                    if(building == null) {
                        _uiState.value = SchematicsDetailUiState.Error("설계도를 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialIcons = building.material.keys.distinct().mapNotNull { name ->
                        ItemRepository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    val materialLocations = building.material.keys.distinct().mapNotNull { name ->
                        ItemRepository.findItemLocation(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = SchematicsDetailUiState.Success(building, materialIcons, materialLocations)
                }
                .onFailure { _uiState.value = SchematicsDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}