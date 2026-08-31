package com.mackereldev.pallogv2.ui.materialDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MaterialDetailUiState {
    object Loading : MaterialDetailUiState()
    data class Success(val material: Item, val materialIcons: Map<String, String>) : MaterialDetailUiState()
    data class Error(val message: String) : MaterialDetailUiState()
}

@HiltViewModel
class MaterialDetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MaterialDetailUiState>(MaterialDetailUiState.Loading)
    val uiState: StateFlow<MaterialDetailUiState> = _uiState.asStateFlow()

    fun loadMaterial(href: String) {
        _uiState.value = MaterialDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getMaterialItem(href) }
                .onSuccess { material ->
                    if (material == null) {
                        _uiState.value = MaterialDetailUiState.Error("소재를 찾을 수 없습니다")
                        return@onSuccess
                    }
                    val materialIcons = material.material.keys.distinct().mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = MaterialDetailUiState.Success(material, materialIcons)
                }
                .onFailure { _uiState.value = MaterialDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}