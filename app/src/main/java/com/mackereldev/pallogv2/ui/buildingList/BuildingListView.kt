package com.mackereldev.pallogv2.ui.buildingList

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
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.ui.components.BuildingCard
import com.mackereldev.pallogv2.ui.components.BuildingSuitabilityCard
import com.mackereldev.pallogv2.ui.components.ProductionCard
import com.mackereldev.pallogv2.ui.components.SchematicsCard
import com.mackereldev.pallogv2.ui.components.StorageCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingListView(
    uiState: BuildingListUiState,
    selectedCategory: BuildingCategory,
    onCategorySelect: (BuildingCategory) -> Unit,
    onMenuClick: () -> Unit,
    onSearchClick : () -> Unit,
    onProductionClick: (String) -> Unit,
    onBuildingSuitabilityClick: (BuildingCategory, String) -> Unit,
    onStorageClick: (String) -> Unit,
    onSchematicsClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("건축물 도감") },
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
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
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
                    BuildingCategory.entries.forEach { category ->
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

            when(uiState) {
                is BuildingListUiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is BuildingListUiState.Error -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text(uiState.message) }

                is BuildingListUiState.Success -> LazyColumn {
                    items(uiState.buildings) { building ->
                        when (selectedCategory) {
                            BuildingCategory.PRODUCTION -> ProductionCard(
                                building = building,
                                onClick = { onProductionClick(building.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            BuildingCategory.PAL,
                            BuildingCategory.FOOD,
                            BuildingCategory.INFRA,
                            BuildingCategory.LIGHTING,
                            BuildingCategory.FOUNDATION,
                            BuildingCategory.DEFENSES,
                            BuildingCategory.OTHER,
                            BuildingCategory.FURNITURE -> BuildingSuitabilityCard(
                                building = building,
                                category = selectedCategory,
                                onClick = { onBuildingSuitabilityClick(selectedCategory, building.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            BuildingCategory.STORAGE -> StorageCard(
                                building = building,
                                onClick = { onStorageClick(building.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            BuildingCategory.SCHEMATICS -> SchematicsCard(
                                building = building,
                                onClick = { onSchematicsClick(building.href) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}