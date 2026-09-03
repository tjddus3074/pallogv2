package com.mackereldev.pallogv2.ui.ItemList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.ui.components.AccessoryCard
import com.mackereldev.pallogv2.ui.components.AmmoCard
import com.mackereldev.pallogv2.ui.components.ArmorCard
import com.mackereldev.pallogv2.ui.components.ItemCard
import com.mackereldev.pallogv2.ui.components.MaterialCard
import com.mackereldev.pallogv2.ui.components.SphereCard
import com.mackereldev.pallogv2.ui.components.SphereModuleCard
import com.mackereldev.pallogv2.ui.components.StatItemCard
import com.mackereldev.pallogv2.ui.components.WeaponCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListView(
    uiState: ItemListUiState,
    selectedCategory: ItemCategory,
    onCategorySelect: (ItemCategory) -> Unit,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onWeaponClick: (String) -> Unit,
    onAmmoClick: (String) -> Unit,
    onArmorClick: (String) -> Unit,
    onSphereClick: (String) -> Unit,
    onSphereModuleClick: (String) -> Unit,
    onAccessoryClick: (String) -> Unit,
    onMaterialClick: (String) -> Unit,
    onConsumableClick: (String) -> Unit,
    onIngredientClick: (String) -> Unit,
    onKeyItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("아이템 도감") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "검색")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            //dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {expanded = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                TextField(
                    value = selectedCategory.displayName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ItemCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.displayName) },
                            onClick = {
                                onCategorySelect(category)
                                expanded = false
                            }
                        )
                    }
                }
            }

            //list
            when(uiState) {
                is ItemListUiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is ItemListUiState.Error -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text(uiState.message)}

                is ItemListUiState.Success -> LazyColumn {
                    when (val categoryItems = uiState.items) {
                        is CategoryItems.Weapons -> items(categoryItems.items) { weapon ->
                            WeaponCard(
                                weapon = weapon,
                                onClick = { onWeaponClick(weapon.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.Ammo -> items(categoryItems.items) { ammo ->
                            AmmoCard(
                                ammo = ammo,
                                onClick = { onAmmoClick(ammo.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.Armor -> items(categoryItems.items) { armor ->
                            ArmorCard(
                                armor = armor,
                                onClick = { onArmorClick(armor.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.Sphere -> items(categoryItems.items) { sphere ->
                            SphereCard(
                                sphere = sphere,
                                onClick = { onSphereClick(sphere.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.SphereModule -> items(categoryItems.items) { module ->
                            SphereModuleCard(
                                module = module,
                                onClick = { onSphereModuleClick(module.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.Accessories -> items(categoryItems.items) { accessory ->
                            AccessoryCard(
                                accessory = accessory,
                                onClick = { onAccessoryClick(accessory.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.Materials -> items(categoryItems.items) { material ->
                            MaterialCard(
                                material = material,
                                onClick = { onMaterialClick(material.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.StatItems -> items(categoryItems.items) { item ->
                            StatItemCard(
                                item = item,
                                category = selectedCategory,
                                onClick = {
                                    when (selectedCategory) {
                                        ItemCategory.CONSUMABLE -> onConsumableClick(item.href)
                                        ItemCategory.INGREDIENT -> onIngredientClick(item.href)
                                        ItemCategory.KEY_ITEM -> onKeyItemClick(item.href)
                                        else -> {}
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is CategoryItems.Generic -> items(categoryItems.items) { item ->
                            ItemCard(
                                item = item,
                                category = selectedCategory,
                                onClick = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}