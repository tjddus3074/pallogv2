package com.mackereldev.pallogv2.ui.ItemList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.AccessoryItem
import com.mackereldev.pallogv2.data.model.AmmoItem
import com.mackereldev.pallogv2.data.model.ArmorItem
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.SphereItem
import com.mackereldev.pallogv2.data.model.SphereModuleItem
import com.mackereldev.pallogv2.data.model.WeaponItem
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CategoryItems{
    data class Generic(val items: List<Item>) : CategoryItems()
    data class Weapons(val items: List<WeaponItem>) : CategoryItems()
    data class Ammo(val items: List<AmmoItem>) : CategoryItems()
    data class Armor(val items: List<ArmorItem>) : CategoryItems()
    data class Sphere(val items: List<SphereItem>) : CategoryItems()
    data class SphereModule(val items: List<SphereModuleItem>) : CategoryItems()
    data class Accessories(val items: List<AccessoryItem>) : CategoryItems()
    data class Materials(val items: List<Item>) : CategoryItems()
    data class StatItems(val items: List<Item>) : CategoryItems()
}

sealed class ItemListUiState {
    object Loading : ItemListUiState()
    data class Success(val items: CategoryItems, val category: ItemCategory) : ItemListUiState()
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
            runCatching {
                when(category) {
                    ItemCategory.WEAPON -> CategoryItems.Weapons(repository.getWeapons())
                    ItemCategory.AMMO -> CategoryItems.Ammo(repository.getAmmo())
                    ItemCategory.ARMOR -> CategoryItems.Armor(repository.getArmor())
                    ItemCategory.SPHERE -> CategoryItems.Sphere(repository.getSphere())
                    ItemCategory.SPHERE_MODULE -> CategoryItems.SphereModule(repository.getSphereModule())
                    ItemCategory.ACCESSORY -> CategoryItems.Accessories(repository.getAccessories())
                    ItemCategory.MATERIAL -> CategoryItems.Materials(repository.getItems(category))
                    ItemCategory.CONSUMABLE,
                    ItemCategory.INGREDIENT,
                    ItemCategory.KEY_ITEM -> CategoryItems.StatItems(repository.getItems(category))
                    else -> CategoryItems.Generic(repository.getItems(category))
                }
            }
                .onSuccess { _uiState.value = ItemListUiState.Success(it, category) }
                .onFailure { _uiState.value = ItemListUiState.Error(it.message ?: "오류 발생") }
        }
    }
}