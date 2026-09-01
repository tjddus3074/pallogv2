package com.mackereldev.pallogv2.ui.storageDetail

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

sealed class StorageDetailUiState {
    object Loading : StorageDetailUiState()
    data class Success(
        val building: Building,
        val materialIcons: Map<String, String>,
        val materialLocations: Map<String, Pair<ItemCategory, String>>
    ) : StorageDetailUiState()
    data class Error(val message: String) : StorageDetailUiState()
}

@HiltViewModel
class StorageDetailViewModel @Inject constructor(
    private val buildingRepository: BuildingRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StorageDetailUiState>(StorageDetailUiState.Loading)
    val uiState: StateFlow<StorageDetailUiState> = _uiState.asStateFlow()

    fun loadBuilding(href: String) {
        _uiState.value = StorageDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { buildingRepository.getBuilding(BuildingCategory.STORAGE, href) }
                .onSuccess { building ->
                    if (building == null) {
                        _uiState.value = StorageDetailUiState.Error("수납 건축물을 찾을 수 없습니다")
                        return@onSuccess
                    }
                    val materialIcons = building.material.keys.distinct().mapNotNull { name ->
                        itemRepository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    val materialLocations = building.material.keys.distinct().mapNotNull { name ->
                        itemRepository.findItemLocation(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = StorageDetailUiState.Success(building, materialIcons, materialLocations)
                }
                .onFailure { _uiState.value = StorageDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}
