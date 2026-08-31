package com.mackereldev.pallogv2.ui.palDetail

import android.view.RoundedCorner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.model.PalDrop
import com.mackereldev.pallogv2.data.model.PalSkill
import com.mackereldev.pallogv2.ui.components.PalScaling
import com.mackereldev.pallogv2.ui.components.elementBorderBrush
import com.mackereldev.pallogv2.ui.components.elementBorderColor
import com.mackereldev.pallogv2.ui.components.elementkotoeng
import com.mackereldev.pallogv2.ui.components.worktoimgpath
import com.mackereldev.pallogv2.ui.theme.Atk2
import com.mackereldev.pallogv2.ui.theme.GoldAccent
import com.mackereldev.pallogv2.ui.theme.PalDef2
import com.mackereldev.pallogv2.ui.theme.PalHP2
import com.mackereldev.pallogv2.ui.theme.SoftGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PalDetailView(
    uiState: PalDetailUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is PalDetailUiState.Success) Text(uiState.pal.palname)
                },
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
                is PalDetailUiState.Loading -> CircularProgressIndicator()
                is PalDetailUiState.Error -> Text(uiState.message)
                is PalDetailUiState.Success -> PalDetailContent(pal = uiState.pal)
            }
        }
    }
}

@Composable
private fun PalDetailContent(pal : Pal) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PalHeaderSection(pal)
        Spacer(modifier = Modifier.height(20.dp))
        PalDescriptionSection(pal)
        Spacer(modifier = Modifier.height(16.dp))
        PalStateSection(pal)
        Spacer(modifier = Modifier.height(16.dp))
        PalPartnerSkillSection(pal)
        Spacer(modifier = Modifier.height(16.dp))
        PalSuitabilitySection(pal)
        Spacer(modifier = Modifier.height(16.dp))
        PalSkillSection(pal)
        Spacer(modifier = Modifier.height(16.dp))
        PalDropSection(pal)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// 공통 섹션 카드
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

// 사진+번호+이름
@Composable
private fun PalHeaderSection(pal: Pal) {
    val palTypes = pal.types.distinct().ifEmpty { listOf("무속성") }
    val borderBrush = elementBorderBrush(palTypes)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(96.dp)) {
            AsyncImage(
                model = "file:///android_asset/${pal.iconAssetpath}",
                contentDescription = pal.palname,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(BorderStroke(3.dp, borderBrush), CircleShape)
            )
            AsyncImage(
                model ="file:///android_asset/PAL/Texture/UI/InGame/${elementkotoeng(palTypes[0])}.png",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.35f).align(Alignment.BottomStart)
            )
            if(palTypes.size > 1) {
                AsyncImage(
                    model = "file:///android_asset/PAL/Texture/UI/InGame/${elementkotoeng(palTypes[1])}.png",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.35f).align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            val suffix = if (pal.isVariety == "1") "B" else ""
            Text(
                text = "NO.%03d%s".format(pal.palDeckIndex, suffix),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = pal.palname,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                PalScaling("HP", pal.stats.hp, bgColor = PalHP2)
                PalScaling("ATK", pal.stats.attack, bgColor = Atk2)
                PalScaling("DEF", pal.stats.defense, bgColor = PalDef2)
            }
        }
    }
}

// 설명
@Composable
private fun PalDescriptionSection(pal: Pal) {
    if(pal.description.isBlank()) return
    SectionCard(title = "설명") {
        Text(text = pal.description, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

// 스펙
@Composable
private fun PalStateSection(pal: Pal) {
    val stats = pal.stats
    SectionCard(title = "스펙") {
        SpecRow("사이즈", stats.size)
        SpecRow("희귀도", stats.rarity)
        SpecRow("HP", stats.hp)
        SpecRow("공격", stats.attack)
        SpecRow("방어", stats.defense)
        SpecRow("근접 공격", stats.meleeAttack)
        SpecRow("작업 속도", stats.workSpeed)
        SpecRow("서포트", stats.support)
        SpecRow("포획 보정", stats.captureRate)
        SpecRow("수컷 확률", if (stats.maleProbability.isBlank()) "" else "${stats.maleProbability}%")
        SpecRow("식사량", pal.foodamount.toString())
        SpecRow("가격", stats.price)
        SpecRow("알 종류", stats.egg)
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

// 파트너 스킬
@Composable
private fun PalPartnerSkillSection(pal: Pal) {
    if(pal.partnerSkill.isBlank()) return
    SectionCard(title = "파트너 스킬") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val fileName = pal.partnerSkillImage.substringAfterLast("/")
            AsyncImage(
                model = "file:///android_asset/PAL/Icon/images_partnerskill/$fileName",
                contentDescription = pal.partnerSkill,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1C1C1C))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = pal.partnerSkill, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = pal.partnerSkillDescription, fontSize = 13.sp, lineHeight = 18.sp)
    }
}

// 작업 적성
@Composable
private fun PalSuitabilitySection(pal: Pal) {
    if(pal.suitability.isEmpty()) return
    SectionCard(title = "작업 적성(기본레벨 ~ 농축레벨)") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pal.suitability.forEach { (key, value) ->
                val work = worktoimgpath(key)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1C1C1C))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    AsyncImage(
                        model = "file:///android_asset/PAL/Texture/UI/InGame/${work}.png",
                        contentDescription = key,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = key, fontSize = 10.sp, color = Color.White)
                    Text(
                        text = buildAnnotatedString {
                            append("Lv.${value.baseLevel} ~ ")
                            withStyle(SpanStyle(color = GoldAccent, fontWeight = FontWeight.Bold)) {
                                append("${value.maxLevel}")
                            }
                        },
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// 스킬
@Composable
private fun PalSkillSection(pal: Pal) {
    if(pal.skills.isEmpty()) return
    SectionCard(title = "스킬") {
        pal.skills.forEachIndexed { index, skill ->
            PalSkillRow(skill)
            if(index != pal.skills.lastIndex) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun PalSkillRow(skill: PalSkill) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(elementBorderColor(skill.skilltype))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = skill.skillname, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = " Lv.${skill.skilllevel}", fontSize = 12.sp, color = Color.Gray)
            }
            Text(
                text = "위력 ${skill.power} · 쿨타임 ${skill.cooltime}초",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
        if(skill.skilldescription.isNotBlank()) {
            Text(
                text = skill.skilldescription,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, start = 16.dp)
            )
        }
    }
}

// 드롭 아이템
@Composable
private fun PalDropSection(pal: Pal) {
    if(pal.drops.isEmpty()) return
    SectionCard(title = "드롭 아이템") {
        pal.drops.forEachIndexed { index, drop ->
            PalDropRow(drop)
            if(index != pal.drops.lastIndex) {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun PalDropRow(drop: PalDrop) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = drop.dropitem, fontSize = 13.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Text(text = "x${drop.quantity}", fontSize = 12.sp, color = Color.Gray)
            Text(text = drop.dropprobability, fontSize = 12.sp, color = Color.Gray)
        }
    }
}