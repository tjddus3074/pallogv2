package com.mackereldev.pallogv2.ui.buildingSuitabilityDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.mackereldev.pallogv2.data.model.Building
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.ui.components.statLabel
import com.mackereldev.pallogv2.ui.theme.SoftGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingSuitabilityDetailView(
    uiState: BuildingSuitabilityDetailUiState,
    onBack: () -> Unit,
    onItemClick: (ItemCategory, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (uiState is BuildingSuitabilityDetailUiState.Success) Text(uiState.building.name) },
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
                is BuildingSuitabilityDetailUiState.Loading -> CircularProgressIndicator()
                is BuildingSuitabilityDetailUiState.Error -> Text(uiState.message)
                is BuildingSuitabilityDetailUiState.Success -> BuildingSuitabilityDetailContent(
                    building = uiState.building,
                    category = uiState.category,
                    materialIcons = uiState.mateiralIcons,
                    materialLocations = uiState.materialLocations,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun BuildingSuitabilityDetailContent(
    building: Building,
    category: BuildingCategory,
    materialIcons: Map<String, String>,
    materialLocations: Map<String, Pair<ItemCategory, String>>,
    onItemClick: (ItemCategory, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        BuildingSuitabilityHeaderSection(building, category)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = building.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        BuildingSuitabilitySection(building)
        Spacer(modifier = Modifier.height(16.dp))
        BuildingSuitabilityDescriptionSection(building)
        Spacer(modifier = Modifier.height(16.dp))
        BuildingSuitabilityMaterialSection(building, materialIcons, materialLocations, onItemClick)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BuildingSuitabilityHeaderSection(building: Building, category: BuildingCategory) {
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
                model = building.iconAssetPath(category),
                contentDescription = building.name,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (building.dynamic.isNotEmpty()) {
            Column(modifier = Modifier.weight(0.3f)) {
                building.dynamic.entries.forEachIndexed { index, (key, value) ->
                    if (index > 0) Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${statLabel(key)} $value", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun BuildingSuitabilitySection(building: Building) {
    if(building.requiredSuitability.isEmpty()) return
    SectionCard(title = "필요 적성") {
        building.requiredSuitability.forEachIndexed { index, req ->
            if(index > 0) Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = req.localIconPath,
                    contentDescription = req.koreanLabel,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${req.koreanLabel} Lv.${req.level}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun BuildingSuitabilityDescriptionSection(building: Building) {
    if(building.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = building.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun BuildingSuitabilityMaterialSection(
    building: Building,
    materialIcons: Map<String, String>,
    materialLocations: Map<String, Pair<ItemCategory, String>>,
    onItemClick: (ItemCategory, String) -> Unit
) {
    if(building.material.isEmpty()) return
    SectionCard(title = "제작 재료") {
        building.material.entries.forEachIndexed { index, (name, amount) ->
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
            if(index != building.material.size - 1) Spacer(modifier = Modifier.height(6.dp))
        }
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