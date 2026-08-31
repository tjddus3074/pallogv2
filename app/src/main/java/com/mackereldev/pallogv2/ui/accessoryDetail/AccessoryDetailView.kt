package com.mackereldev.pallogv2.ui.accessoryDetail

import androidx.compose.foundation.background
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
import com.mackereldev.pallogv2.data.model.AccessoryItem
import com.mackereldev.pallogv2.data.model.AccessoryLevel
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.ui.components.weaponRarityColor
import com.mackereldev.pallogv2.ui.theme.SoftGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessoryDetailView (
    uiState: AccessoryDetailUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (uiState is AccessoryDetailUiState.Success) Text(uiState.accessory.name) },
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
                is AccessoryDetailUiState.Loading -> CircularProgressIndicator()
                is AccessoryDetailUiState.Error -> Text(uiState.message)
                is AccessoryDetailUiState.Success -> AccessoryDetailContent(accessory = uiState.accessory, materialIcons = uiState.materialIcons)
            }
        }
    }
}

@Composable
private fun AccessoryDetailContent(accessory: AccessoryItem, materialIcons: Map<String, String>) {
    val level = accessory.effectiveLevels.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (level != null) {
            AccessoryHeaderSection(accessory = accessory, level = level)
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = accessory.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                if (level.rarity.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(weaponRarityColor(level.rarity))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = level.rarity, fontSize = 11.sp, color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AccessoryDescriptionSection(level)
            Spacer(modifier = Modifier.height(16.dp))
            AccessoryMaterialSection(level, materialIcons)
            Spacer(modifier = Modifier.height(16.dp))
            AccessoryProductionSection(accessory)
            Spacer(modifier = Modifier.height(16.dp))
            AccessoryOtherSpecSection(level)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AccessoryHeaderSection(accessory: AccessoryItem, level: AccessoryLevel) {
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
                model = accessory.iconAssetPath(ItemCategory.ACCESSORY),
                contentDescription = accessory.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(0.3f)) {
            if (level.skill.isNotBlank()) {
                Text(text = level.skill, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            if (level.techLevel.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "기술 Lv.${level.techLevel}", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun AccessoryDescriptionSection(level: AccessoryLevel) {
    if (level.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = level.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun AccessoryMaterialSection(level: AccessoryLevel, materialIcons: Map<String, String>) {
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
            if (index != level.material.size - 1) Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun AccessoryProductionSection(accessory: AccessoryItem) {
    if(accessory.production.isEmpty()) return
    SectionCard(title = "생산 시설") {
        accessory.production.forEachIndexed { index, facility ->
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
            if(index != accessory.production.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AccessoryOtherSpecSection(level: AccessoryLevel) {
    SectionCard(title = "그 외 스펙") {
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
    if(value.isBlank()) return
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}