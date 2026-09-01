package com.mackereldev.pallogv2.ui.ingredientDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.FacilityLocation
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.ui.components.statLabel
import com.mackereldev.pallogv2.ui.theme.SoftGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientDetailView(
    uiState: IngredientDetailUiState,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (uiState is IngredientDetailUiState.Success) Text(uiState.ingredient.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is IngredientDetailUiState.Loading -> CircularProgressIndicator()
                is IngredientDetailUiState.Error -> Text(uiState.message)
                is IngredientDetailUiState.Success -> IngredientDetailContent(
                    ingredient = uiState.ingredient,
                    materialIcons = uiState.materialIcons,
                    materialLocations = uiState.materialLocations,
                    productionFacilities = uiState.productionFacilities,
                    onItemClick = onItemClick,
                    onProductionClick = onProductionClick
                )
            }
        }
    }
}

@Composable
private fun IngredientDetailContent(
    ingredient: Item,
    materialIcons: Map<String, String>,
    materialLocations: Map<String, Pair<ItemCategory, String>>,
    productionFacilities: Map<String, FacilityLocation>,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IngredientHeaderSection(ingredient)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = ingredient.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        IngredientDescriptionSection(ingredient)
        Spacer(modifier = Modifier.height(16.dp))
        IngredientMaterialSection(ingredient, materialIcons, materialLocations, onItemClick)
        Spacer(modifier = Modifier.height(16.dp))
        IngredientProductionSection(ingredient, productionFacilities, onProductionClick)
        Spacer(modifier = Modifier.height(16.dp))
        IngredientOtherSpecSection(ingredient)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun IngredientHeaderSection(ingredient: Item) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.7f).aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "file:///android_asset/PAL/Icon/menu_icons/new_item_bg.png",
                contentDescription = null,
                colorFilter = ColorFilter.tint(SoftGray),
                modifier = Modifier.fillMaxSize()
            )
            AsyncImage(
                model = ingredient.iconAssetPath(ItemCategory.INGREDIENT),
                contentDescription = ingredient.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (ingredient.dynamic.isNotEmpty()) {
            Column(modifier = Modifier.weight(0.3f)) {
                ingredient.dynamic.entries.forEachIndexed { index, (key, value) ->
                    if (index > 0) Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${statLabel(key)} $value", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun IngredientDescriptionSection(ingredient: Item) {
    if(ingredient.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = ingredient.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun IngredientMaterialSection(
    ingredient: Item,
    materialIcons: Map<String, String>,
    materialLocations: Map<String, Pair<ItemCategory, String>>,
    onItemClick: (ItemCategory, String) -> Unit
) {
    if(ingredient.material.isEmpty()) return
    SectionCard(title = "제작 재료") {
        ingredient.material.entries.forEachIndexed { index, (name, amount) ->
            val location = materialLocations[name]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .let { base -> if (location != null) base.clickable { onItemClick(location.first, location.second) } else base },
                verticalAlignment = Alignment.CenterVertically
            ) {
                materialIcons[name]?.let { iconPath ->
                    AsyncImage(
                        model = iconPath,
                        contentDescription = name,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1C1C1C))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = name, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                Text(text = "x$amount", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            if(index != ingredient.material.size - 1) Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun IngredientProductionSection(
    ingredient: Item,
    productionFacilities: Map<String, FacilityLocation>,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    if(ingredient.production.isEmpty()) return
    SectionCard(title = "생산 시설") {
        ingredient.production.forEachIndexed { index, facility ->
            val location = productionFacilities[facility.pname]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .let { base -> if (location != null) base.clickable { onProductionClick(location.category, location.href) } else base },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = location?.iconPath
                        ?: "file:///android_asset/PAL/Icon/images_production/${facility.imgsrc.substringAfterLast("/")}",
                    contentDescription = facility.pname,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1C1C1C))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = facility.pname, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            if(index != ingredient.production.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun IngredientOtherSpecSection(ingredient: Item) {
    SectionCard(title = "그 외 스펙") {
        SpecRow("가격", ingredient.금화)
        SpecRow("무게", ingredient.weight)
        SpecRow("최대 소지 개수", ingredient.maxStackCount)
        SpecRow("기습 공격 배율", ingredient.sneakAttackRate)
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(SoftGray.copy(alpha = 0.1f))
                .padding(12.dp),
            content = content
        )
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    if(value.isBlank()) return
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
