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
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.SphereModuleItem
import com.mackereldev.pallogv2.ui.theme.SoftGray

@Composable
fun SphereModuleCard(
    module: SphereModuleItem,
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
                    model = module.iconAssetPath(ItemCategory.SPHERE_MODULE),
                    contentDescription = module.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(1.dp, SoftGray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = module.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                if(module.rarity.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(weaponRarityColor(module.rarity))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = module.rarity, fontSize = 10.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 모듈 특성 + 포획력 강화 + 기술 레벨
            Column(
                horizontalAlignment = Alignment.End
            ) {
                module.trait?.let { trait ->
                    Text(text = trait, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                module.captureBonus?.let { bonus ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = bonus, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                if(module.techLevel.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "기술 Lv.${module.techLevel}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}