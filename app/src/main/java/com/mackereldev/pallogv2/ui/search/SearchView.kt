package com.mackereldev.pallogv2.ui.search

import android.R.attr.type
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.repository.SearchNavTarget
import com.mackereldev.pallogv2.data.repository.SearchResult
import com.mackereldev.pallogv2.ui.components.elementkotoeng
import com.mackereldev.pallogv2.ui.components.worktoimgpath
import com.mackereldev.pallogv2.ui.theme.SoftGray
import com.mackereldev.pallogv2.ui.theme.TechLevel

private val ALL_WORK_TYPES = listOf(
    "수작업", "채굴", "벌목", "관개", "불 피우기", "제약",
    "냉각", "채집", "파종", "발전", "운반", "목장"
)
private val ALL_ELEMENT_TYPES = listOf(
    "화염 속성", "물 속성", "번개 속성", "풀 속성", "얼음 속성",
    "땅 속성", "어둠 속성", "용 속성", "무속성"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    mode: SearchMode,
    query: String,
    results: List<SearchResult>,
    suitabilityFilters: List<SuitabilityFilter>,
    selectedTypes: Set<String>,
    filteredPals: List<Pal>,
    onMenuClick: () -> Unit,
    onModeSelect: (SearchMode) -> Unit,
    onQueryChange: (String) -> Unit,
    onAddSuitabilityFilter: (String) -> Unit,
    onSuitabilityLevelChange: (String, Int) -> Unit,
    onRemoveSuitabilityFilter: (String) -> Unit,
    onToggleType: (String) -> Unit,
    onResultClick: (SearchResult) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("검색") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
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
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModeButton("통합 검색(팰, 아이템)", mode == SearchMode.NAME, { onModeSelect(SearchMode.NAME) }, Modifier.weight(1f))
                ModeButton("팰 상세 검색", mode == SearchMode.PAL_FILTER, { onModeSelect(SearchMode.PAL_FILTER) }, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            when (mode) {
                SearchMode.NAME -> NameSearchContent(query, results, onQueryChange, onResultClick)
                SearchMode.PAL_FILTER -> PalFilterContent(
                    suitabilityFilters = suitabilityFilters,
                    selectedTypes = selectedTypes,
                    filteredPals = filteredPals,
                    onAddSuitabilityFilter = onAddSuitabilityFilter,
                    onSuitabilityLevelChange = onSuitabilityLevelChange,
                    onRemoveSuitabilityFilter = onRemoveSuitabilityFilter,
                    onToggleType = onToggleType,
                    onResultClick = onResultClick
                )
            }
        }
    }
}

@Composable
private fun ModeButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) TechLevel else SoftGray.copy(alpha = 0.15f)
        )
    ) {
        Text(text = text, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
    }
}

@Composable
private fun NameSearchContent(
    query: String,
    results: List<SearchResult>,
    onQueryChange: (String) -> Unit,
    onResultClick: (SearchResult) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("이름을 입력하세요") },
        singleLine = true,
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, SoftGray, RoundedCornerShape(15.dp))
    )

    Spacer(modifier = Modifier.padding(top = 12.dp))

    when {
        query.isBlank() -> CenteredHint("검색어를 입력하세요")
        results.isEmpty() -> CenteredHint("검색 결과가 없습니다")
        else -> LazyColumn { items(results) { result -> SearchResultRow(result, onResultClick) } }
    }
}

@Composable
private fun PalFilterContent(
    suitabilityFilters: List<SuitabilityFilter>,
    selectedTypes: Set<String>,
    filteredPals: List<Pal>,
    onAddSuitabilityFilter: (String) -> Unit,
    onSuitabilityLevelChange: (String, Int) -> Unit,
    onRemoveSuitabilityFilter: (String) -> Unit,
    onToggleType: (String) -> Unit,
    onResultClick: (SearchResult) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val remainingWorkTypes = ALL_WORK_TYPES.filterNot { type -> suitabilityFilters.any { it.workType == type } }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "작업 적성 (모두 만족)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))

        suitabilityFilters.forEach { filter ->
            SuitabilityFilterRow(
                filter = filter,
                onLevelChange = { level -> onSuitabilityLevelChange(filter.workType, level) },
                onRemove = { onRemoveSuitabilityFilter(filter.workType) }
            )
            Spacer(Modifier.height(6.dp))
        }

        if (remainingWorkTypes.isNotEmpty()) {
            TextButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("작업 적성 추가")
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(text = "속성 (하나라도 포함)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ALL_ELEMENT_TYPES.forEach { type ->
                TypeChip(type = type, selected = type in selectedTypes, onClick = { onToggleType(type) })
            }
        }

        Spacer(Modifier.height(16.dp))

        if (suitabilityFilters.isEmpty() && selectedTypes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("조건을 선택하면 팰 목록이 표시됩니다", color = Color.Gray)
            }
        } else {
            Text(text = "결과 (${filteredPals.size}마리)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            LazyColumn {
                items(filteredPals) { pal -> SearchResultRow(pal.toSearchResult(), onResultClick) }
            }
        }
    }

    if (showAddDialog) {
        AddWorkFilterDialog(
            availableTypes = remainingWorkTypes,
            onDismiss = { showAddDialog = false },
            onSelect = { work ->
                onAddSuitabilityFilter(work)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun SuitabilityFilterRow(filter: SuitabilityFilter, onLevelChange: (Int) -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoftGray.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val workIcon = worktoimgpath(filter.workType)
        if (workIcon.isNotBlank()) {
            AsyncImage(
                model = "file:///android_asset/PAL/Texture/UI/InGame/$workIcon.png",
                contentDescription = filter.workType,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(text = filter.workType, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))

        IconButton(
            onClick = { if (filter.minLevel > 1) onLevelChange(filter.minLevel - 1) },
            modifier = Modifier.size(28.dp)
        ) { Icon(Icons.Default.Remove, contentDescription = "레벨 감소") }

        Text(text = "Lv.${filter.minLevel}+", fontSize = 13.sp, fontWeight = FontWeight.Bold)

        IconButton(
            onClick = { if (filter.minLevel < 8) onLevelChange(filter.minLevel + 1) },
            modifier = Modifier.size(28.dp)
        ) { Icon(Icons.Default.Add, contentDescription = "레벨 증가") }

        IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "삭제")
        }
    }
}

@Composable
private fun TypeChip(type: String, selected: Boolean, onClick: () -> Unit) {
    val icon = elementkotoeng(type)
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) TechLevel.copy(alpha = 0.25f) else SoftGray.copy(alpha = 0.1f))
            .border(1.dp, if(selected) TechLevel else SoftGray.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(icon.isNotBlank()) {
            AsyncImage(
                model = "file:///android_asset/PAL/Texture/UI/InGame/$icon.png",
                contentDescription = type,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(text = type.removeSuffix(" 속성"), fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AddWorkFilterDialog(
    availableTypes: List<String>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss)  { Text("닫기") } },
        title = { Text("작업 적성 추가") },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 360.dp)) {
                items(availableTypes) { work ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(work) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = worktoimgpath(work)
                        if(icon.isNotEmpty()) {
                            AsyncImage(
                                model = "file:///android_asset/PAL/Texture/UI/InGame/$icon.png",
                                contentDescription = work,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(text = work, fontSize = 14.sp)
                    }
                }
            }
        }
    )
}

@Composable
private fun CenteredHint(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text ,color = Color.Gray)
    }
}

@Composable
private fun SearchResultRow(result: SearchResult, onClick: (SearchResult) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(SoftGray.copy(alpha = 0.1f))
            .border(1.dp, SoftGray.copy(alpha = 0.12f), RoundedCornerShape(15.dp))
            .clickable { onClick(result) }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.15f)
                .aspectRatio(1f)
        ) {
            AsyncImage(
                model = result.iconPath,
                contentDescription = result.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(1.dp, SoftGray, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = result.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

private fun Pal.toSearchResult(): SearchResult = SearchResult(
    name = palname,
    iconPath = "file:///android_asset/$iconAssetpath",
    target = SearchNavTarget.PalDetail(stats.code)
)