package com.mackereldev.pallogv2.ui.tech

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.TechItem
import com.mackereldev.pallogv2.data.model.TechRow
import com.mackereldev.pallogv2.ui.theme.SoftGray
import com.mackereldev.pallogv2.ui.theme.TechLevel
import com.mackereldev.pallogv2.ui.theme.tech_boss

private const val COLUMNS = 4
private val LEVEL_BADGE_SIZE = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechView(
    uiState: TechUiState,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("기술") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is TechUiState.Loading -> CircularProgressIndicator()
                is TechUiState.Error -> Text(uiState.message)
                is TechUiState.Success -> TechList(rows = uiState.rows, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
private fun TechList(rows: List<TechRow>, onItemClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(rows) { row -> TechRowCard(row, onItemClick) }
    }
}

@Composable
private fun TechRowCard(row: TechRow, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(SoftGray.copy(alpha = 0.1f))
            .border(1.dp, SoftGray.copy(alpha = 0.12f), RoundedCornerShape(15.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(LEVEL_BADGE_SIZE),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "file:///android_asset/PAL/Texture/UI/InGame/T_prt_icon_circle_f.png",
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = row.level,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        VerticalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            thickness = 1.dp,
            color = TechLevel.copy(alpha = 0.5f)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row.items.chunked(COLUMNS).forEach { lineItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    lineItems.forEach { item ->
                        TechItemCell(
                            item = item,
                            onClick = { onItemClick(item.itemName) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(COLUMNS - lineItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun TechItemCell(item: TechItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val borderColor = if (item.isBossTechnology) tech_boss else TechLevel

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.iconAssetPath,
                contentDescription = item.itemName,
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }
        Text(
            text = item.itemName,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            fontSize = when {
                item.itemName.length > 12 -> 8.sp
                item.itemName.length > 9 -> 9.sp
                item.itemName.length > 6 -> 10.sp
                else -> 11.sp
            },
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}