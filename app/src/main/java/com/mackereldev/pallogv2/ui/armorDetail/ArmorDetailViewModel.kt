package com.mackereldev.pallogv2.ui.armorDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.ArmorItem
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

sealed class ArmorDetailUiState {
    object Loading : ArmorDetailUiState()
    data class Success(
        val armor : ArmorItem,
        val materialIcons : Map<String, String>,
        val materialLocations : Map<String, Pair<ItemCategory, String>>,
        val productionFacilities : Map<String, FacilityLocation>
    ) : ArmorDetailUiState()
    data class Error(val message : String) : ArmorDetailUiState()
}

@HiltViewModel
class ArmorDetailViewModel @Inject constructor(
    private val repository: ItemRepository,
    private val buildingRepository: BuildingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArmorDetailUiState>(ArmorDetailUiState.Loading)
    val uiState : StateFlow<ArmorDetailUiState> = _uiState.asStateFlow()

    fun loadArmor(href: String) {
        _uiState.value = ArmorDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getArmorItem(href) }
                .onSuccess { armor ->
                    if(armor == null){
                        _uiState.value = ArmorDetailUiState.Error("방어구를 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialNames = armor.effectiveLevels.flatMap { it.material.keys }.distinct()
                    val materialIcons = materialNames.mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    val materialLocations = materialNames.mapNotNull { name ->
                        repository.findItemLocation(name)?.let { name to it }
                    }.toMap()
                    val productionFacilities = armor.production.map { it.pname }.distinct().mapNotNull { name ->
                        buildingRepository.findFacilityLocation(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = ArmorDetailUiState.Success(armor, materialIcons, materialLocations, productionFacilities)
                }
                .onFailure { _uiState.value = ArmorDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }

}
