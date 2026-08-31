package com.mackereldev.pallogv2.ui.keyItemDetail

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

sealed class KeyItemDetailUiState {
    object Loading : KeyItemDetailUiState()
    data class Success(val keyItem: Item, val materialIcons: Map<String, String>) : KeyItemDetailUiState()
    data class Error(val message: String) : KeyItemDetailUiState()
}

@HiltViewModel
class KeyItemDetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<KeyItemDetailUiState>(KeyItemDetailUiState.Loading)
    val uiState: StateFlow<KeyItemDetailUiState> = _uiState.asStateFlow()

    fun loadKeyItem(href: String) {
        _uiState.value = KeyItemDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getKeyItem(href) }
                .onSuccess { keyItem ->
                    if (keyItem == null) {
                        _uiState.value = KeyItemDetailUiState.Error("귀중품을 찾을 수 없습니다")
                        return@onSuccess
                    }
                    val materialIcons = keyItem.material.keys.distinct().mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = KeyItemDetailUiState.Success(keyItem, materialIcons)
                }
                .onFailure { _uiState.value = KeyItemDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}