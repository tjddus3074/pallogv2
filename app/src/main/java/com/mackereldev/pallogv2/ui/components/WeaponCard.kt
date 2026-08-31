package com.mackereldev.pallogv2.ui.components

import android.widget.Space
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
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.WeaponItem
import com.mackereldev.pallogv2.ui.theme.SoftGray
import com.mackereldev.pallogv2.ui.theme.rarity_0
import com.mackereldev.pallogv2.ui.theme.rarity_1
import com.mackereldev.pallogv2.ui.theme.rarity_2
import com.mackereldev.pallogv2.ui.theme.rarity_3
import com.mackereldev.pallogv2.ui.theme.rarity_4

@Composable
fun WeaponCard(
    weapon: WeaponItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp).clickable{ onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(SoftGray.copy(alpha = 0.1f))
                .border(1.dp, SoftGray.copy(alpha = 0.12f), RoundedCornerShape(15.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f)) {
                AsyncImage(
                    model = weapon.iconAssetPath(ItemCategory.WEAPON),
                    contentDescription = weapon.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(1.dp, SoftGray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // 이름 + 등급 배지
            Column(modifier = Modifier.weight(1f)) {
                Text(text = weapon.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    weapon.rarities.forEach { rarity ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(weaponRarityColor(rarity))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = rarity, fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 공격력 / 해금 기술 레벨
            Column(horizontalAlignment = Alignment.End) {
                val attack = weapon.baseLevel?.attack.orEmpty()
                if(attack.isNotBlank()) {
                    Text(
                        text = "공격력 ${weapon.baseLevel?.attack.orEmpty()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (weapon.techLevel.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "기술 Lv.${weapon.techLevel}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
fun weaponRarityColor(rarity: String): Color = when (rarity) {
    "일반" -> rarity_0
    "비범" -> rarity_1
    "희귀" -> rarity_2
    "영웅" -> rarity_3
    "전설" -> rarity_4
    else -> rarity_0
}