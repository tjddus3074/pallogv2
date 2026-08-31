package com.mackereldev.pallogv2.ui.ingredientDetail

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

sealed class IngredientDetailUiState {
    object Loading : IngredientDetailUiState()
    data class Success(val ingredient: Item, val materialIcons: Map<String, String>) : IngredientDetailUiState()
    data class Error(val message: String) : IngredientDetailUiState()
}

@HiltViewModel
class IngredientDetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<IngredientDetailUiState>(IngredientDetailUiState.Loading)
    val uiState: StateFlow<IngredientDetailUiState> = _uiState.asStateFlow()

    fun loadIngredient(href: String) {
        _uiState.value = IngredientDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getIngredientItem(href) }
                .onSuccess { ingredient ->
                    if(ingredient == null) {
                        _uiState.value = IngredientDetailUiState.Error("식재료를 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val materialIcons = ingredient.material.keys.distinct().mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = IngredientDetailUiState.Success(ingredient, materialIcons)
                }
                .onFailure { _uiState.value = IngredientDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}