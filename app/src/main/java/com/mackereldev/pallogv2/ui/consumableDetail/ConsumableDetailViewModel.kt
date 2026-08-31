package com.mackereldev.pallogv2.ui.consumableDetail

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

sealed class ConsumableDetailUiState {
    object Loading : ConsumableDetailUiState()
    data class Success(val consumable: Item, val materialIcons: Map<String, String>) : ConsumableDetailUiState()
    data class Error(val message: String) : ConsumableDetailUiState()
}

@HiltViewModel
class ConsumableDetailViewModel @Inject constructor(
   private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConsumableDetailUiState>(ConsumableDetailUiState.Loading)
    val uiState : StateFlow<ConsumableDetailUiState> = _uiState.asStateFlow()

    fun loadConsumable(href: String) {
        _uiState.value = ConsumableDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getConsumableItem(href) }
                .onSuccess { consumable ->
                    if(consumable == null) {
                        _uiState.value = ConsumableDetailUiState.Error("소모품을 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialIcons = consumable.material.keys.distinct().mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = ConsumableDetailUiState.Success(consumable, materialIcons)
                }
                .onFailure { _uiState.value = ConsumableDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}