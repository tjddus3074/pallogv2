package com.mackereldev.pallogv2.ui.sphereDetail

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
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.SphereItem
import com.mackereldev.pallogv2.data.model.SphereLevel
import com.mackereldev.pallogv2.ui.components.weaponRarityColor
import com.mackereldev.pallogv2.ui.theme.SoftGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SphereDetailView(
    uiState: SphereDetailUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (uiState is SphereDetailUiState.Success) Text(uiState.sphere.name) },
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
                is SphereDetailUiState.Loading -> CircularProgressIndicator()
                is SphereDetailUiState.Error -> Text(uiState.message)
                is SphereDetailUiState.Success -> SphereDetailContent(sphere = uiState.sphere, materialIcons = uiState.materialIcons)
            }
        }
    }
}

@Composable
private fun SphereDetailContent(sphere: SphereItem, materialIcons: Map<String, String>) {
    var selectedRarity by remember(sphere.href) { mutableStateOf(sphere.rarities.firstOrNull()) }
    val selectedLevel = sphere.effectiveLevels.firstOrNull { it.rarity == selectedRarity }
        ?: sphere.effectiveLevels.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (sphere.rarities.size > 1) {
            GradeSelector(
                rarities = sphere.rarities,
                selected = selectedRarity,
                onSelect = { selectedRarity = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (selectedLevel != null) {
            SphereHeaderSection(sphere = sphere, level = selectedLevel)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = sphere.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            SphereDescriptionSection(selectedLevel)
            Spacer(modifier = Modifier.height(16.dp))
            SphereMaterialSection(selectedLevel, materialIcons)
            Spacer(modifier = Modifier.height(16.dp))
            SphereProductionSection(sphere)
            Spacer(modifier = Modifier.height(16.dp))
            SphereOtherSpecSection(selectedLevel)
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
private fun SphereHeaderSection(sphere: SphereItem, level: SphereLevel) {
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
                model = sphere.iconAssetPath(ItemCategory.SPHERE),
                contentDescription = sphere.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(0.3f)) {
            if(level.captureRate.isNotBlank()) {
                Text(text = "포획력 ${level.captureRate}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
            if(level.techLevel.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "기술 Lv.${level.techLevel}", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun SphereDescriptionSection(level: SphereLevel) {
    if(level.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = level.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun SphereMaterialSection(level: SphereLevel, materialIcons: Map<String, String>) {
    if(level.material.isEmpty()) return
    SectionCard(title = "제작 재료") {
        level.material.entries.forEachIndexed { index, (name, amount) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
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
private fun SphereProductionSection(sphere: SphereItem) {
    if(sphere.production.isEmpty()) return
    SectionCard(title = "생산 시설") {
        sphere.production.forEachIndexed { index, facility ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "file:///android_asset/PAL/Icon/images_production/${facility.imgsrc.substringAfterLast("/")}",
                    contentDescription = facility.pname,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1C1C1C))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = facility.pname, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            if(index != sphere.production.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SphereOtherSpecSection(level: SphereLevel) {
    SectionCard(title = "그 외 스펙") {
        SpecRow("가격", level.price)
        SpecRow("무게", level.weight)
        SpecRow("최대 소지 개수", level.maxStackCount)
        SpecRow("배면 공격 배율", level.sneakAttackRate)
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

