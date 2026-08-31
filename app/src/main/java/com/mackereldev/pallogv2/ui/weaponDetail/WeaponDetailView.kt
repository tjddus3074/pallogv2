package com.mackereldev.pallogv2.ui.weaponDetail

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.materialIcon
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.AmmoItem
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.WeaponItem
import com.mackereldev.pallogv2.data.model.WeaponLevel
import com.mackereldev.pallogv2.ui.components.weaponRarityColor
import com.mackereldev.pallogv2.ui.theme.SoftGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponDetailView(
    uiState: WeaponDetailUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is WeaponDetailUiState.Success) Text(uiState.weapon.name)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) {
        padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is WeaponDetailUiState.Loading -> CircularProgressIndicator()
                is WeaponDetailUiState.Error -> Text(uiState.message)
                is WeaponDetailUiState.Success -> WeaponDetailContent(weapon = uiState.weapon, ammo = uiState.ammo,
                    materialIcons = uiState.materialIcons
                )
            }
        }
    }
}

@Composable
private fun WeaponDetailContent(weapon: WeaponItem, ammo: AmmoItem?, materialIcons: Map<String, String>) {
    var selectedRarity by remember(weapon.href) { mutableStateOf(weapon.rarities.firstOrNull()) }
    val selectedLevel = weapon.effectiveLevels.firstOrNull { it.rarity == selectedRarity }
        ?: weapon.effectiveLevels.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 등급 버튼 (등급 없거나 단일 등급이면 생략)
        if (weapon.rarities.size > 1) {
            GradeSelector(
                rarities = weapon.rarities,
                selected = selectedRarity,
                onSelect = { selectedRarity = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (selectedLevel != null) {
            WeaponHeaderSection(weapon = weapon, level = selectedLevel)
            Spacer(modifier = Modifier.height(20.dp))
            WeaponNameSection(weapon.name)
            Spacer(modifier = Modifier.height(16.dp))
            WeaponDescriptionSection(selectedLevel)
            Spacer(modifier = Modifier.height(16.dp))
            AmmoSection(ammo)
            Spacer(modifier = Modifier.height(16.dp))
            MaterialSection(selectedLevel, materialIcons)
            Spacer(modifier = Modifier.height(16.dp))
            ProductionSection(weapon)
            Spacer(modifier = Modifier.height(16.dp))
            OtherSpecSection(selectedLevel)
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
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
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
private fun WeaponHeaderSection(weapon: WeaponItem, level: WeaponLevel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "file:///android_asset/PAL/Icon/menu_icons/new_item_bg.png",
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.fillMaxSize()
            )
            AsyncImage(
                model = weapon.iconAssetPath(ItemCategory.WEAPON),
                contentDescription = weapon.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(0.3f)) {
            if (level.attack.isNotBlank()) {
                Text(text = "공격력 ${level.attack}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
            if (level.techLevel.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "기술 Lv.${level.techLevel}", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun WeaponNameSection(name: String) {
    Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun WeaponDescriptionSection(level: WeaponLevel) {
    if(level.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = level.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun AmmoSection(ammo: AmmoItem?) {
    if(ammo == null) return
    SectionCard(title = "사용 탄약") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ammo.iconAssetPath(ItemCategory.AMMO),
                contentDescription = ammo.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.dp, SoftGray, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = ammo.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                if(ammo.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = ammo.description, fontSize = 12.sp, color = Color.Gray, lineHeight = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun MaterialSection(level: WeaponLevel, materialIcons: Map<String, String>) {
    if (level.meterial.isEmpty()) return
    SectionCard(title = "제작 재료") {
        level.meterial.entries.forEachIndexed { index, (name, amount) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconPath = materialIcons[name]
                if (iconPath != null) {
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
                Text(
                    text = name,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "x$amount", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            if (index != level.meterial.size - 1) Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun ProductionSection(weapon: WeaponItem) {
    if (weapon.production.isEmpty()) return
    SectionCard(title = "생산 시설") {
        weapon.production.forEachIndexed { index, facility ->
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
            if (index != weapon.production.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun OtherSpecSection(level: WeaponLevel) {
    SectionCard(title = "기타 스펙") {
        SpecRow("내구도", level.durability)
        SpecRow("가격", level.price)
        SpecRow("무게", level.weight)
        SpecRow("최대 소지 개수", level.maxStackCount)
        SpecRow("탄창 크기", level.magazineSize)
        SpecRow("기습 공격 배율", level.sneakAttackRate)
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(SoftGray.copy(alpha = 0.1f))
                .border(1.dp, SoftGray.copy(alpha = 0.12f), RoundedCornerShape(15.dp))
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