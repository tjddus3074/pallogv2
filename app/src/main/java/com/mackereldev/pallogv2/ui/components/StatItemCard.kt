package com.mackereldev.pallogv2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.ui.theme.SoftGray

@Composable
fun StatItemCard(
    item: Item,
    category: ItemCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp).clickable { onClick() },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = item.iconAssetPath(category),
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(1.dp, SoftGray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            if(item.dynamic.isNotEmpty()) {
                Column(horizontalAlignment = Alignment.End) {
                    item.dynamic.entries.forEachIndexed { index, (key, value) ->
                        if(index > 0) Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${statLabel(key)} $value",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// json dynamic 키 -> 표시 라벨. 매핑에 없는 키는 원문 그대로 표시
val statLabels = mapOf(
    "기술" to "기술 Lv",
    "기술 포인트" to "기술 포인트",
    "영양가" to "영양가",
    "SAN" to "SAN",
    "SANResist" to "SAN 내성",
    "회복 시간" to "회복 시간",
    "HP 회복" to "HP 회복",
    "작업 속도" to "작업 속도",
    "공격" to "공격",
    "방어" to "방어",
    "Exp" to "경험치",
    "Exp_Increase" to "경험치 증가",
    "MaxHP" to "최대 HP",
    "MaxSP" to "최대 SP",
    "Power" to "작업 효율",
    "WorkSpeed" to "작업 속도",
    "MaxInventoryWeight" to "소지 무게 증가",
    "HP IV" to "HP 개체값",
    "Attack IV" to "공격 개체값",
    "Defense IV" to "방어 개체값",
    "HungerResist" to "허기 내성",
    "ExplosionResist" to "폭발 내성",
    "SearchProbabilityRate" to "탐색 확률",
    "HitBarSizeRate" to "히트바 크기",
    "EnemyAddDropPercent" to "적 드랍률 증가",
    "ItemLotteryAddDropPercent" to "아이템 드랍률 증가",
    "FullStomachKeep" to "포만감 유지",
    "LeanBackAndKnockbackInvalid" to "넉백 무효",
    "SAN Rate" to "SAN 회복률",
    "Healing" to "치유량",
    "Energy" to "소모 전력",
)

fun statLabel(key: String): String = statLabels[key] ?: key