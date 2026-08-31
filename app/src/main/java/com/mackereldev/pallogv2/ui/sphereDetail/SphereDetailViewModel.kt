package com.mackereldev.pallogv2.ui.sphereDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.SphereItem
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SphereDetailUiState {
    object Loading : SphereDetailUiState()
    data class Success(val sphere: SphereItem, val materialIcons: Map<String, String>) : SphereDetailUiState()
    data class Error(val message: String) : SphereDetailUiState()
}

@HiltViewModel
class SphereDetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SphereDetailUiState>(SphereDetailUiState.Loading)
    val uiState: StateFlow<SphereDetailUiState> = _uiState.asStateFlow()

    fun loadSphere(href: String) {
        _uiState.value = SphereDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO){
            runCatching { repository.getSphereItem(href) }
                .onSuccess { sphere ->
                    if(sphere == null) {
                        _uiState.value = SphereDetailUiState.Error("스피어를 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialNames = sphere.effectiveLevels.flatMap { it.material.keys }.distinct()
                    val materialIcons = materialNames.mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = SphereDetailUiState.Success(sphere, materialIcons)
                }
                .onFailure { _uiState.value = SphereDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}