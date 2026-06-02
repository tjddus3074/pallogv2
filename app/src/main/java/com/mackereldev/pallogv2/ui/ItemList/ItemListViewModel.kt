package com.mackereldev.pallogv2.ui.ItemList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ItemListUiState {
    object Loading : ItemListUiState()
    data class Success(val items: List<Item>, val category: ItemCategory) : ItemListUiState()
    data class Error(val message: String) : ItemListUiState()
}

@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemListUiState>(ItemListUiState.Loading)
    val uiState: StateFlow<ItemListUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ItemCategory.WEAPON)
    val selectedCategory: StateFlow<ItemCategory> = _selectedCategory.asStateFlow()

    init { loadItems(ItemCategory.WEAPON) }

    fun selectCategory(category: ItemCategory) {
        _selectedCategory.value = category
        loadItems(category)
    }

    private fun loadItems(category: ItemCategory) {
        _uiState.value = ItemListUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getItems(category) }
                .onSuccess { _uiState.value = ItemListUiState.Success(it, category) }
                .onFailure { _uiState.value = ItemListUiState.Error(it.message ?: "오류 발생") }
        }
    }
}