package com.mackereldev.pallogv2.ui.sphereModuleDetail

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
import androidx.compose.runtime.getValue
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
import com.mackereldev.pallogv2.data.model.SphereModuleItem
import com.mackereldev.pallogv2.ui.components.weaponRarityColor
import com.mackereldev.pallogv2.ui.theme.SoftGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SphereModuleDetailView(
    uiState: SphereModuleDetailUiState,
    onBack: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (uiState is SphereModuleDetailUiState.Success) Text(uiState.module.name) },
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
                is SphereModuleDetailUiState.Loading -> CircularProgressIndicator()
                is SphereModuleDetailUiState.Error -> Text(uiState.message)
                is SphereModuleDetailUiState.Success -> SphereModuleDetailContent(module = uiState.module, materialIcons = uiState.materialIcons)
            }
        }
    }
}

@Composable
private fun SphereModuleDetailContent(module: SphereModuleItem, materialIcons: Map<String, String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SphereModuleHeaderSection(module = module)
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = module.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (module.rarity.isNotBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(weaponRarityColor(module.rarity))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = module.rarity, fontSize = 11.sp, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SphereModuleDescriptionSection(module)
        Spacer(modifier = Modifier.height(16.dp))
        SphereModuleEffectSection(module)
        Spacer(modifier = Modifier.height(16.dp))
        SphereModuleMaterialSection(module, materialIcons)
        Spacer(modifier = Modifier.height(16.dp))
        SphereModuleProductionSection(module)
        Spacer(modifier = Modifier.height(16.dp))
        SphereModuleOtherSpecSection(module)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SphereModuleHeaderSection(module: SphereModuleItem) {
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
                model = module.iconAssetPath(ItemCategory.SPHERE_MODULE),
                contentDescription = module.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(0.3f)) {
            module.trait?.let { trait ->
                Text(text = trait, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            module.captureBonus?.let { bonus ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = bonus, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            if (module.techLevel.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "기술 Lv.${module.techLevel}", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun SphereModuleDescriptionSection(module: SphereModuleItem) {
    if (module.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = module.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun SphereModuleEffectSection(module: SphereModuleItem) {
    if (module.skill.isEmpty()) return
    SectionCard(title = "모듈 효과") {
        module.skill.forEachIndexed { index, effect ->
            Text(text = "· $effect", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            if (index != module.skill.lastIndex) Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun SphereModuleMaterialSection(module: SphereModuleItem, materialIcons: Map<String, String>) {
    if (module.material.isEmpty()) return
    SectionCard(title = "제작 재료") {
        module.material.entries.forEachIndexed { index, (name, amount) ->
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
            if (index != module.material.size - 1) Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun SphereModuleProductionSection(module: SphereModuleItem) {
    if (module.production.isEmpty()) return
    SectionCard(title = "생산 시설") {
        module.production.forEachIndexed { index, facility ->
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
            if (index != module.production.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SphereModuleOtherSpecSection(module: SphereModuleItem) {
    SectionCard(title = "그 외 스펙") {
        SpecRow("가격", module.price)
        SpecRow("무게", module.weight)
        SpecRow("최대 소지 개수", module.maxStackCount)
        SpecRow("기습 공격 배율", module.sneakAttackRate)
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