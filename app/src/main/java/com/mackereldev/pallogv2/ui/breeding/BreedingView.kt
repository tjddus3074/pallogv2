package com.mackereldev.pallogv2.ui.breeding

import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.ui.theme.GoldAccent
import com.mackereldev.pallogv2.ui.theme.SoftGray
import com.mackereldev.pallogv2.ui.theme.TechLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedingView(
    uiState: BreedingUiState,
    mode: BreedingMode,
    parent1: Pal?,
    parent2: Pal?,
    child: Pal?,
    material: Pal?,
    comboFilter: Pal?,
    childResults: List<Pal>,
    resultCombos: List<UniqueComboUi>,
    parentPairs: List<ParentPairUi>,
    combos: List<UniqueComboUi>,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onModeSelect: (BreedingMode) -> Unit,
    onParent1Select: (Pal?) -> Unit,
    onParent2Select: (Pal?) -> Unit,
    onChildSelect: (Pal?) -> Unit,
    onMaterialSelect: (Pal?) -> Unit,
    onComboFilterSelect: (Pal?) -> Unit,
    onPalClick: (Pal) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("교배") },
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
                is BreedingUiState.Loading -> CircularProgressIndicator()
                is BreedingUiState.Error -> Text(uiState.message)
                is BreedingUiState.Success -> BreedingContent(
                    pals = uiState.pals,
                    mode = mode,
                    parent1 = parent1,
                    parent2 = parent2,
                    child = child,
                    material = material,
                    comboFilter = comboFilter,
                    childResults = childResults,
                    resultCombos = resultCombos,
                    parentPairs = parentPairs,
                    combos = combos,
                    onModeSelect = onModeSelect,
                    onParent1Select = onParent1Select,
                    onParent2Select = onParent2Select,
                    onChildSelect = onChildSelect,
                    onMaterialSelect = onMaterialSelect,
                    onComboFilterSelect = onComboFilterSelect,
                    onPalClick = onPalClick
                )
            }
        }
    }
}

@Composable
private fun BreedingContent(
    pals: List<Pal>,
    mode: BreedingMode,
    parent1: Pal?,
    parent2: Pal?,
    child: Pal?,
    material: Pal?,
    comboFilter: Pal?,
    childResults: List<Pal>,
    resultCombos: List<UniqueComboUi>,
    parentPairs: List<ParentPairUi>,
    combos: List<UniqueComboUi>,
    onModeSelect: (BreedingMode) -> Unit,
    onParent1Select: (Pal?) -> Unit,
    onParent2Select: (Pal?) -> Unit,
    onChildSelect: (Pal?) -> Unit,
    onMaterialSelect: (Pal?) -> Unit,
    onComboFilterSelect: (Pal?) -> Unit,
    onPalClick: (Pal) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ModeButton(
                    "부모→자식",
                    mode == BreedingMode.PARENT_TO_CHILD,
                    { onModeSelect(BreedingMode.PARENT_TO_CHILD) },
                    Modifier.weight(1f)
                )
                ModeButton(
                    "자식→부모",
                    mode == BreedingMode.CHILD_TO_PARENT,
                    { onModeSelect(BreedingMode.CHILD_TO_PARENT) },
                    Modifier.weight(1f)
                )
                ModeButton(
                    "특수 조합",
                    mode == BreedingMode.UNIQUE_COMBO,
                    { onModeSelect(BreedingMode.UNIQUE_COMBO) },
                    Modifier.weight(1f)
                )
            }
        }

        when (mode) {
            BreedingMode.PARENT_TO_CHILD -> {
                item {
                    SectionCard(title = "부모 팰 선택") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PalSelector("부모 1", parent1, pals, onParent1Select)
                            Icon(Icons.Default.Add, null, Modifier.size(28.dp), tint = TechLevel)
                            PalSelector("부모 2", parent2, pals, onParent2Select)
                        }
                    }
                }

                if (childResults.isNotEmpty()) {
                    item {
                        SectionCard(title = if (resultCombos.isNotEmpty()) "예상 결과 (특수 조합)" else "예상 결과") {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (resultCombos.isNotEmpty()) {
                                    resultCombos.forEach { combo ->
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            PalCircle(combo.child, 110) { onPalClick(combo.child) }
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                palLabel(combo.child),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (combo.parent1Gender.isNotBlank() || combo.parent2Gender.isNotBlank()) {
                                                Spacer(Modifier.height(4.dp))
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Text(
                                                        combo.parent1.palname,
                                                        fontSize = 12.sp,
                                                        color = GoldAccent
                                                    )
                                                    GenderIcon(combo.parent1Gender, size = 14)
                                                    Text("+", fontSize = 12.sp, color = GoldAccent)
                                                    Text(
                                                        combo.parent2.palname,
                                                        fontSize = 12.sp,
                                                        color = GoldAccent
                                                    )
                                                    GenderIcon(combo.parent2Gender, size = 14)
                                                    Text("필요", fontSize = 12.sp, color = GoldAccent)
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    childResults.forEach { result ->
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            PalCircle(result, 110) { onPalClick(result) }
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                palLabel(result),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            BreedingMode.CHILD_TO_PARENT -> {
                item {
                    SectionCard(title = "자식 팰 선택") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PalSelector("재료 팰(선택)", material, pals, onMaterialSelect, allowClear = true, circleSize = 80)
                            PalSelector("자식 팰", child, pals, onChildSelect)
                        }
                    }
                }

                if (child != null) {
                    item {
                        Text(
                            text = if (material != null) "재료 팰 포함 조합 (${parentPairs.size}개)"
                            else "가능한 부모 조합 (${parentPairs.size}개)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    if (parentPairs.isEmpty()) {
                        item {
                            Text(
                                text = "가능한 부모 조합이 없습니다.",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(parentPairs) { pair -> ParentPairRow(pair, onPalClick) }
                    }
                }
            }

            BreedingMode.UNIQUE_COMBO -> {
                item {
                    SectionCard(title = "특수 조합 (${combos.size}개)") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            PalSelector("팰로 검색(선택)", comboFilter, pals, onComboFilterSelect, allowClear = true, circleSize = 80)
                        }
                    }
                }
                items(combos) { combo -> UniqueComboRow(combo, onPalClick) }
            }
        }
    }
}

@Composable
private fun UniqueComboRow(combo: UniqueComboUi, onPalClick: (Pal) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(SoftGray.copy(alpha = 0.1f))
            .border(1.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(15.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PalMini(combo.parent1, combo.parent1Gender) { onPalClick(combo.parent1) }
            Icon(Icons.Default.Add, null, Modifier.size(16.dp), tint = TechLevel)
            PalMini(combo.parent2, combo.parent2Gender) { onPalClick(combo.parent2) }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(16.dp), tint = GoldAccent)
            PalMini(combo.child) { onPalClick(combo.child) }
        }
    }
}


@Composable
private fun ModeButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) TechLevel else SoftGray.copy(alpha = 0.15f)
        )
    ) {
        Text(text = text, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = Color.White)
    }
}

@Composable
private fun PalSelector(
    label: String,
    selected: Pal?,
    pals: List<Pal>,
    onSelect: (Pal?) -> Unit,
    allowClear: Boolean = false,
    circleSize: Int = 110
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)

        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .clip(CircleShape)
                .border(2.dp, if (selected != null) TechLevel else SoftGray, CircleShape)
                .clickable { showPicker = true },
            contentAlignment = Alignment.Center
        ) {
            if (selected != null) {
                AsyncImage(
                    model = "file:///android_asset/${selected.iconAssetpath}",
                    contentDescription = selected.palname,
                    modifier = Modifier.size((circleSize - 10).dp).clip(CircleShape)
                )
            } else {
                Icon(Icons.Default.Add, "팰 선택", tint = SoftGray, modifier = Modifier.size(28.dp))
            }
        }

        Text(
            text = selected?.let { palLabel(it) } ?: "팰 선택",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }

    if (showPicker) {
        PalPickerDialog(
            pals = pals,
            allowClear = allowClear,
            onDismiss = { showPicker = false },
            onSelect = {
                onSelect(it)
                showPicker = false
            }
        )
    }
}

    @Composable
private fun PalPickerDialog(
    pals: List<Pal>,
    allowClear: Boolean,
    onDismiss: () -> Unit,
    onSelect: (Pal?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("닫기") } },
        title = { Text("팰 선택") },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 420.dp)) {
                if (allowClear) {
                    item {
                        Text(
                            text = "선택 해제",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(null) }
                                .padding(vertical = 10.dp)
                        )
                    }
                }
                items(pals) { pal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(pal) }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "file:///android_asset/${pal.iconAssetpath}",
                            contentDescription = pal.palname,
                            modifier = Modifier.size(36.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(text = palLabel(pal), fontSize = 14.sp)
                    }
                }
            }
        }
    )
}

@Composable
private fun ParentPairRow(pair: ParentPairUi, onPalClick: (Pal) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(SoftGray.copy(alpha = 0.1f))
            .border(1.dp, SoftGray.copy(alpha = 0.12f), RoundedCornerShape(15.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PalMini(pair.parent1, pair.parent1Gender) { onPalClick(pair.parent1) }
        Icon(Icons.Default.Add, null, Modifier.size(18.dp), tint = TechLevel)
        PalMini(pair.parent2, pair.parent2Gender) { onPalClick(pair.parent2) }
    }
}

@Composable
private fun PalMini(pal: Pal, gender: String = "", onClick: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box {
            PalCircle(pal, 60, onClick)
            if (gender.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1C1C1C))
                        .border(1.dp, SoftGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    GenderIcon(gender, size = 13)
                }
            }
        }
        Text(text = palLabel(pal), fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun PalCircle(pal: Pal, size: Int, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(1.dp, SoftGray, CircleShape)
            .let { if (onClick != null) it.clickable { onClick() } else it },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = "file:///android_asset/${pal.iconAssetpath}",
            contentDescription = pal.palname,
            modifier = Modifier.size((size - 8).dp).clip(CircleShape)
        )
    }
}

@Composable
private fun GenderIcon(gender: String, size: Int) {
    val fileName = when (gender) {
        "Male" -> "T_Icon_PanGender_Male"
        "Female" -> "T_Icon_PanGender_Female"
        else -> return
    }
    AsyncImage(
        model = "file:///android_asset/PAL/Icon/menu_icons/$fileName.png",
        contentDescription = gender,
        modifier = Modifier.size(size.dp)
    )
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Column {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(SoftGray.copy(alpha = 0.1f))
                .padding(12.dp)
        ) { content() }
    }
}

// NO.001 도롱이 / 변종은 뒤에 B
private fun palLabel(pal: Pal): String {
    val no = "NO.%03d".format(pal.palDeckIndex)
    val suffix = if (pal.isVariety == "1") "B" else ""
    return "$no$suffix ${pal.palname}"
}