package com.mackereldev.pallogv2.ui.armorDetail

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.ArmorItem
import com.mackereldev.pallogv2.data.model.ArmorLevel
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.FacilityLocation
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.ui.components.weaponRarityColor
import com.mackereldev.pallogv2.ui.theme.SoftGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArmorDetailView(
    uiState: ArmorDetailUiState,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (uiState is ArmorDetailUiState.Success) Text(uiState.armor.name) },
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
            when (uiState) {
                is ArmorDetailUiState.Loading -> CircularProgressIndicator()
                is ArmorDetailUiState.Error -> Text(uiState.message)
                is ArmorDetailUiState.Success -> ArmorDetailContent(
                    armor = uiState.armor,
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
private fun ArmorDetailContent(
    armor: ArmorItem,
    materialIcons: Map<String, String>,
    materialLocations: Map<String, Pair<ItemCategory, String>>,
    productionFacilities: Map<String, FacilityLocation>,
    onItemClick: (ItemCategory, String) -> Unit,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    var selectedRarity by remember(armor.href) { mutableStateOf(armor.rarities.firstOrNull()) }
    val selectedLevel = armor.effectiveLevels.firstOrNull { it.rarity == selectedRarity }
        ?: armor.effectiveLevels.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (armor.rarities.size > 1) {
            GradeSelector(
                rarities = armor.rarities,
                selected = selectedRarity,
                onSelect = { selectedRarity = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (selectedLevel != null) {
            ArmorHeaderSection(armor = armor, level = selectedLevel)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = armor.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            ArmorDescriptionSection(selectedLevel)
            Spacer(modifier = Modifier.height(16.dp))
            ArmorMaterialSection(selectedLevel, materialIcons, materialLocations, onItemClick)
            Spacer(modifier = Modifier.height(16.dp))
            ArmorProductionSection(armor, productionFacilities, onProductionClick)
            Spacer(modifier = Modifier.height(16.dp))
            ArmorOtherSpecSection(selectedLevel)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun GradeSelector(
    rarities: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rarities.forEach { rarity ->
            val isSelected = rarity == selected
            val color = weaponRarityColor(rarity)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) color else color.copy(alpha = 0.25f))
                    .clickable { onSelect(rarity) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rarity,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ArmorHeaderSection(armor: ArmorItem, level: ArmorLevel) {
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
                model = armor.iconAssetPath(ItemCategory.ARMOR),
                contentDescription = armor.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(0.3f)) {
            if (level.isShield) {
                if (level.shield.isNotBlank()) {
                    Text(text = "방패 ${level.shield}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            } else {
                if (level.hp.isNotBlank()) {
                    Text(text = "HP ${level.hp}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
                if (level.defense.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "방어 ${level.defense}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
            if (level.techLevel.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "기술 Lv.${level.techLevel}", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun ArmorDescriptionSection(level: ArmorLevel) {
    if (level.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = level.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun ArmorMaterialSection(
    level: ArmorLevel,
    materialIcons: Map<String, String>,
    materialLocations: Map<String, Pair<ItemCategory, String>>,
    onItemClick: (ItemCategory, String) -> Unit
) {
    if(level.material.isEmpty()) return
    SectionCard(title = "제작 재료") {
        level.material.entries.forEachIndexed { index, (name, amount) ->
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
            if(index != level.material.size - 1) Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun ArmorProductionSection(
    armor: ArmorItem,
    productionFacilities: Map<String, FacilityLocation>,
    onProductionClick: (BuildingCategory, String) -> Unit
) {
    if(armor.production.isEmpty()) return
    SectionCard(title = "생산 시설") {
        armor.production.forEachIndexed { index, facility ->
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
                Text(text = facility.pname, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            if(index != armor.production.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ArmorOtherSpecSection(level: ArmorLevel) {
    SectionCard(title = "그 외 스펙") {
        SpecRow("내구도", level.durability)
        SpecRow("가격", level.price)
        SpecRow("무게", level.weight)
        SpecRow("최대 소지 개수", level.maxStackCount)
        SpecRow("기습 공격 배율", level.sneakAttackRate)
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
    if (value.isBlank()) return
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
