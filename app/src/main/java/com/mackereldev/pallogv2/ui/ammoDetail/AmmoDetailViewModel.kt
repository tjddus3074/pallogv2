package com.mackereldev.pallogv2.ui.ammoDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.AmmoItem
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AmmoDetailUiState {
    object Loading : AmmoDetailUiState()
    data class Success(val ammo: AmmoItem, val materialIcons: Map<String, String>) : AmmoDetailUiState()
    data class Error(val message: String) : AmmoDetailUiState()
}

@HiltViewModel
class AmmoDetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AmmoDetailUiState>(AmmoDetailUiState.Loading)
    val uiState: StateFlow<AmmoDetailUiState> = _uiState.asStateFlow()

    fun loadAmmo(href: String) {
        _uiState.value = AmmoDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getAmmoItem(href) }
                .onSuccess { ammo ->
                    if (ammo == null) {
                        _uiState.value = AmmoDetailUiState.Error("탄약을 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialIcons = ammo.meterial.keys.mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = AmmoDetailUiState.Success(ammo, materialIcons)
                }
                .onFailure { _uiState.value = AmmoDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}