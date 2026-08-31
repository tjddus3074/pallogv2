package com.mackereldev.pallogv2.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.ui.theme.Atk2
import com.mackereldev.pallogv2.ui.theme.PalDef2
import com.mackereldev.pallogv2.ui.theme.PalHP2
import com.mackereldev.pallogv2.ui.theme.SoftGray

@Composable
fun PalCard(pal: Pal, onClick: () -> Unit, modifier: Modifier = Modifier) {
//    val borderColor = elementBorderColor(pal.types.firstOrNull() ?: "무속성")
    val palTypes = pal.types.distinct().ifEmpty { listOf("무속성") }
    val borderBrush = elementBorderBrush(palTypes)

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(SoftGray.copy(alpha = 0.1f))
                .border(1.dp, SoftGray.copy(alpha = 0.12f), RoundedCornerShape(15.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 팰 이미지 + 속성 아이콘 오버레이 (비율 기반)
            Box(
                modifier = Modifier.fillMaxWidth(0.2f)
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = "file:///android_asset/${pal.iconAssetpath}",
                    contentDescription = pal.palname,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(BorderStroke(2.5.dp, borderBrush), CircleShape)
                )

                // 첫 번째 속성 아이콘: 기존 위치(좌하단) 유지
                AsyncImage(
                    model = "file:///android_asset/PAL/Texture/UI/InGame/${elementkotoeng(palTypes[0])}.png",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(0.35f)
                        .align(Alignment.BottomStart)
                )

                // 두 번째 속성이 있을 때만 우하단에 추가
                if (palTypes.size > 1) {
                    AsyncImage(
                        model = "file:///android_asset/PAL/Texture/UI/InGame/${elementkotoeng(palTypes[1])}.png",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(0.35f)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val suffix = if (pal.isVariety == "1") "B" else ""
                        Text(
                            text = "NO.%03d%s".format(pal.palDeckIndex, suffix),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = pal.palname,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        PalScaling("HP", pal.stats.hp, bgColor = PalHP2)
                        PalScaling("ATK", pal.stats.attack, bgColor = Atk2)
                        PalScaling("DEF", pal.stats.defense, bgColor = PalDef2)
                    }
                }

                Spacer(modifier = Modifier.padding(5.dp))

                // 작업 적성 다크 배지
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    pal.suitability.forEach { (key, value) ->
                        val work = worktoimgpath(key)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1C1C1C))
                                .padding(horizontal = 5.dp, vertical = 4.dp)
                        ) {
                            AsyncImage(
                                model = "file:///android_asset/PAL/Texture/UI/InGame/${work}.png",
                                contentDescription = key,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = value.baseLevel.toString(),
                                fontSize = 10.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// 속성별 테두리 색상
fun elementBorderColor(type: String): Color = when (type) {
    "풀 속성"   -> Color(0xFF4CAF50)
    "화염 속성" -> Color(0xFFE64A19)
    "물 속성"   -> Color(0xFF1E88E5)
    "번개 속성" -> Color(0xFFFFD600)
    "얼음 속성" -> Color(0xFF80DEEA)
    "땅 속성"   -> Color(0xFF8D6E63)
    "어둠 속성" -> Color(0xFF7B1FA2)
    "용 속성"   -> Color(0xFFAB47BC)
    else        -> Color(0xFF9E9E9E)
}

fun elementBorderBrush(types: List<String>): Brush {
    val colors = types.map { elementBorderColor(it) }
    return when (colors.size) {
        0 -> SolidColor(elementBorderColor("무속성"))
        1 -> SolidColor(colors[0])
        else -> {
            val leftColor = colors[0]   // 좌하단 아이콘과 매칭
            val rightColor = colors[1]  // 우하단 아이콘과 매칭
            Brush.sweepGradient(
                0f to rightColor,    // 3시(오른쪽) = rightColor 정점
                0.5f to leftColor,   // 9시(왼쪽) = leftColor 정점
                1f to rightColor     // 다시 3시로 돌아와 이음매 자연스럽게
            )
        }
    }
}

fun elementkotoeng(em: String): String = when (em) {
    "무속성"   -> "T_Icon_element_s_00"
    "어둠 속성" -> "T_Icon_element_s_05"
    "용 속성"   -> "T_Icon_element_s_06"
    "땅 속성"   -> "T_Icon_element_s_07"
    "번개 속성" -> "T_Icon_element_s_03"
    "화염 속성" -> "T_Icon_element_s_01"
    "얼음 속성" -> "T_Icon_element_s_08"
    "풀 속성"   -> "T_Icon_element_s_04"
    "물 속성"   -> "T_Icon_element_s_02"
    else        -> ""
}

fun worktoimgpath(work: String): String = when (work) {
    "불 피우기" -> "T_icon_palwork_00"
    "관개"     -> "T_icon_palwork_01"
    "파종"     -> "T_icon_palwork_02"
    "발전"     -> "T_icon_palwork_03"
    "수작업"   -> "T_icon_palwork_04"
    "채집"     -> "T_icon_palwork_05"
    "벌목"     -> "T_icon_palwork_06"
    "채굴"     -> "T_icon_palwork_07"
    "제약"     -> "T_icon_palwork_08"
    "냉각"     -> "T_icon_palwork_10"
    "운반"     -> "T_icon_palwork_11"
    "목장"     -> "T_icon_palwork_12"
    else        -> ""
}

@Composable
fun PalScaling(label: String, value: String, bgColor: Color) {
    val stat = when (label) {
        "HP" -> "health"
        "ATK" -> "attack"
        "DEF" -> "defense"
        else -> ""
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor,
        modifier = Modifier.width(65.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            AsyncImage(
                model = "file:///android_asset/PAL/Icon/${stat}.png",
                contentDescription = stat,
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 3.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

//@Composable
//fun PalCard(pal: Pal, onClick: () -> Unit, modifier: Modifier = Modifier) {
//    Card(
//        modifier = modifier
//            .padding(4.dp)
//            .clickable{ onClick() },
//        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth()
//                .clip(RoundedCornerShape(15.dp))
////                .background(MaterialTheme.colorScheme.surface)
//                .background(SoftGray.copy(alpha = 0.15f))
//                .border(1.dp, SoftGray, RoundedCornerShape(15.dp))
//                .padding(10.dp)
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                AsyncImage(
//                    model = "file:///android_asset/${pal.iconAssetpath}",
//                    contentDescription = pal.palname,
//                    modifier = Modifier
//                        .fillMaxWidth(0.2f)
//                        .aspectRatio(1f)
//                        .clip(CircleShape)
//                        .border(1.dp, SoftGray, CircleShape)
//                )
//                Spacer(modifier = Modifier.padding(3.dp))
//
//                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                    pal.types.distinct().forEach { em ->
//                        var emtype = elementkotoeng(em.toString())
//
//                        AsyncImage(
//                            model = "file:///android_asset/PAL/Texture/UI/InGame/${emtype}.png",
//                            contentDescription = pal.palname,
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.padding(5.dp))
//
//            Column {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    if(pal.isVariety == "1") {
//                        Column {
//                            Text(text = "NO.%03d".format(pal.palDeckIndex) + "B ", fontSize = 14.sp)
//                            Text(text = pal.palname, fontSize = 14.sp)
//                        }
//                    } else {
//                        Column {
//                            Text(text = "NO.%03d".format(pal.palDeckIndex) + " ", fontSize = 14.sp)
//                            Text(text = pal.palname, fontSize = 14.sp)
//                        }
//                    }
//
//                    Row(
////                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(3.dp)
//                    ) {
//                        PalScaling("HP", pal.stats.hp, bgColor = PalHP2,)
//                        PalScaling("ATK", pal.stats.attack, bgColor = Atk2)
//                        PalScaling("DEF", pal.stats.defense, bgColor = PalDef2)
//                    }
//                }
//                Spacer(modifier = Modifier.padding(5.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    val maxSlots = 9
//                    val paddedSuitability = pal.suitability.toList() + List(maxSlots - pal.suitability.toList().size) { null }
//
//                    paddedSuitability.forEach{ item ->
//                        Column(
//                            modifier = Modifier.weight(1f),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            if(item != null) {
//                                val (key, value) = item
//                                var work = worktoimgpath(key.toString())
//
//                                AsyncImage(
//                                    model = "file:///android_asset/PAL/Texture/UI/InGame/${work}.png",
//                                    contentDescription = pal.palname,
//                                )
//                                Text(text = "$value",
//                                    fontSize = 12.sp,
//                                    textAlign = TextAlign.Center)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//    Spacer(modifier = Modifier.padding(8.dp))
//}
//
//fun elementkotoeng(em : String): String {
//    if(em.equals("무속성")) {
//        return "T_Icon_element_s_00"
//    } else if(em.equals("어둠 속성")) {
//        return "T_Icon_element_s_05"
//    } else if(em.equals("용 속성")) {
//        return "T_Icon_element_s_06"
//    } else if(em.equals("땅 속성")) {
//        return "T_Icon_element_s_07"
//    } else if(em.equals("번개 속성")) {
//        return "T_Icon_element_s_03"
//    } else if(em.equals("화염 속성")) {
//        return "T_Icon_element_s_01"
//    } else if(em.equals("얼음 속성")) {
//        return "T_Icon_element_s_08"
//    } else if(em.equals("풀 속성")) {
//        return "T_Icon_element_s_04"
//    } else if(em.equals("물 속성")) {
//        return "T_Icon_element_s_02"
//    } else {
//        return "";
//    }
//}
//
//fun worktoimgpath(work : String) : String {
//    if(work.equals("불 피우기")) {
//        return "T_icon_palwork_00"
//    } else if(work.equals("관개")) {
//        return "T_icon_palwork_01"
//    } else if(work.equals("파종")) {
//        return "T_icon_palwork_02"
//    } else if(work.equals("발전")) {
//        return "T_icon_palwork_03"
//    } else if(work.equals("수작업")) {
//        return "T_icon_palwork_04"
//    } else if(work.equals("채집")) {
//        return "T_icon_palwork_05"
//    } else if(work.equals("벌목")) {
//        return "T_icon_palwork_06"
//    } else if(work.equals("채굴")) {
//        return "T_icon_palwork_07"
//    } else if(work.equals("제약")) {
//        return "T_icon_palwork_08"
//    } else if(work.equals("냉각")) {
//        return "T_icon_palwork_10"
//    } else if(work.equals("운반")) {
//        return "T_icon_palwork_11"
//    } else if(work.equals("목장")) {
//        return "T_icon_palwork_12"
//    } else {
//        return ""
//    }
//}
//
//@Composable
//fun PalScaling(label: String, value: String, bgColor: Color) {
//    var stat = ""
//    if(label == "HP") {
//        stat = "health"
//    } else if (label == "ATK") {
//        stat = "attack"
//    } else if(label == "DEF") {
//        stat = "defense"
//    }
//
//    Surface(
//        shape = RoundedCornerShape(50),
//        color = bgColor,
//        modifier = Modifier.width(65.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center,
//            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
//            AsyncImage(
//                model = "file:///android_asset/PAL/Icon/${stat}.png",
//                contentDescription = stat,
//                modifier = Modifier.size(20.dp),
//                contentScale = ContentScale.Fit
//            )
//
//            Text(text = value,
//                modifier = Modifier.padding(horizontal = 3.dp),
//                color = Color.White,
//                fontWeight = FontWeight.Bold)
//        }
//    }
//}
