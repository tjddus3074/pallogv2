package com.mackereldev.pallogv2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mackereldev.pallogv2.data.model.Building
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.ui.theme.SoftGray

@Composable
fun BuildingCard(
    building: Building,
    category: BuildingCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable{ onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(SoftGray.copy(0.15f))
                .border(1.dp, SoftGray, RoundedCornerShape(15.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = building.iconAssetPath(category),
                contentDescription = building.name,
                modifier = Modifier
                    .width(56.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(1.dp, SoftGray, CircleShape),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column{
                Text(text = building.name, fontSize = 14.sp)
                Text(text = building.description, fontSize = 12.sp, maxLines = 2)
            }
        }
    }
}