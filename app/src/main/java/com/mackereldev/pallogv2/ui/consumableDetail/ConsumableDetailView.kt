package com.mackereldev.pallogv2.ui.consumableDetail

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
fun ConsumableDetailView(
    uiState: ConsumableDetailUiState,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { if ( uiState is ConsumableDetailUiState.Success) Text(uiState.consumable.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when(uiState) {
                is ConsumableDetailUiState.Loading -> CircularProgressIndicator()
                is ConsumableDetailUiState.Error -> Text(uiState.message)
                is ConsumableDetailUiState.Success -> ConsumableDetailContent(
                    consumable = uiState.consumable,
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
private fun ConsumableDetailContent(
    consumable: Item,
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
        ConsumableHeaderSection(consumable)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = consumable.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        ConsumableDescriptionSection(consumable)
        Spacer(modifier = Modifier.height(16.dp))
        ConsumableMaterialSection(consumable, materialIcons, materialLocations, onItemClick)
        Spacer(modifier = Modifier.height(16.dp))
        ConsumableProductionSection(consumable, productionFacilities, onProductionClick)
        Spacer(modifier = Modifier.height(16.dp))
        ConsumableOtherSpecSection(consumable)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ConsumableHeaderSection(consumable: Item) {
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
                model = consumable.iconAssetPath(ItemCategory.CONSUMABLE),
                contentDescription = consumable.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (consumable.dynamic.isNotEmpty()) {
            Column(modifier = Modifier.weight(0.3f)) {
                consumable.dynamic.entries.forEachIndexed { index, (key, value) ->
                    if (index > 0) Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${statLabel(key)} $value", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun ConsumableDescriptionSection(consumable: Item) {
    if(consumable.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = consumable.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun ConsumableMaterialSection(
    consumable: Item,
    materialIcons: Map<String, String>,
    materialLocations: Map<String, Pair<ItemCategory, String>>,
    onItemClick: (ItemCategory, String) -> Unit
) {
    if(consumable.material.isEmpty()) return
    SectionCard("제작 재료") {
        consumable.material.entries.forEachIndexed { index, (name, amount) ->
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
            if(index != consumable.material.size - 1) Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun ConsumableProductionSection(
    consumable: Item,
    productionFacilities: Map<String, FacilityLocation>,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    if(consumable.production.isEmpty()) return
    SectionCard(title = "생산 시설") {
        consumable.production.forEachIndexed { index, facility ->
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
            if(index != consumable.production.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ConsumableOtherSpecSection(consumable: Item) {
    SectionCard(title = "그 외 스펙") {
        SpecRow("가격", consumable.금화)
        SpecRow("무게", consumable.weight)
        SpecRow("최대 소지 개수", consumable.maxStackCount)
        SpecRow("기습 공격 배율", consumable.sneakAttackRate)
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
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
