package com.mackereldev.pallogv2.ui.accessoryDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.AccessoryItem
import com.mackereldev.pallogv2.data.model.FacilityLocation
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

sealed class AccessoryDetailUiState {
    object Loading : AccessoryDetailUiState()
    data class Success(
        val accessory: AccessoryItem,
        val materialIcons: Map<String, String>,
        val materialLocations: Map<String, Pair<ItemCategory, String>>,
        val productionFacilities: Map<String, FacilityLocation>
    ) : AccessoryDetailUiState()
    data class Error(val message: String) : AccessoryDetailUiState()
}

@HiltViewModel
class AccessoryDetailViewModel @Inject constructor(
    private val repository: ItemRepository,
    private val buildingRepository: BuildingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccessoryDetailUiState>(AccessoryDetailUiState.Loading)
    val uiState: StateFlow<AccessoryDetailUiState> = _uiState.asStateFlow()

    fun loadAccessory(href: String) {
        _uiState.value = AccessoryDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getAccessoryItem(href) }
                .onSuccess { accessory ->
                    if(accessory == null) {
                        _uiState.value = AccessoryDetailUiState.Error("장신구를 찾을 수 없습니다")
                        return@onSuccess
                    }
                    val materialNames = accessory.effectiveLevels.flatMap { it.material.keys }.distinct()
                    val materialIcons = materialNames.mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    val materialLocations = materialNames.mapNotNull { name ->
                        repository.findItemLocation(name)?.let { name to it }
                    }.toMap()
                    val productionFacilities = accessory.production.map { it.pname }.distinct().mapNotNull { name ->
                        buildingRepository.findFacilityLocation(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = AccessoryDetailUiState.Success(accessory, materialIcons, materialLocations, productionFacilities)
                }
                .onFailure { _uiState.value = AccessoryDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}
