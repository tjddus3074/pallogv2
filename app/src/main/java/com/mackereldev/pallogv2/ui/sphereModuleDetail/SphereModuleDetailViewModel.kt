package com.mackereldev.pallogv2.ui.sphereModuleDetail

import androidx.compose.material.icons.materialIcon
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.SphereModuleItem
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SphereModuleDetailUiState{
    object Loading : SphereModuleDetailUiState()
    data class Success(val module: SphereModuleItem, val materialIcons: Map<String, String>) : SphereModuleDetailUiState()
    data class Error(val message: String) : SphereModuleDetailUiState()
}

@HiltViewModel
class SphereModuleDetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SphereModuleDetailUiState>(SphereModuleDetailUiState.Loading)
    val uiState: StateFlow<SphereModuleDetailUiState> = _uiState.asStateFlow()

    fun loadModule(href: String) {
        _uiState.value = SphereModuleDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getSphereModuleItem(href) }
                .onSuccess { module ->
                    if(module == null) {
                        _uiState.value = SphereModuleDetailUiState.Error("스피어 모듈을 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialIcons = module.material.keys.distinct().mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = SphereModuleDetailUiState.Success(module, materialIcons)
                }
                .onFailure { _uiState.value = SphereModuleDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}