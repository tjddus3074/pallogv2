package com.mackereldev.pallogv2.screen.pal.components

import android.graphics.BitmapFactory
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mackereldev.pallogv2.screen.pal.PalViewModel
import com.mackereldev.pallogv2.ui.theme.OnSurfaceDark
import com.mackereldev.pallogv2.ui.theme.SoftGray

@Composable
fun PalListView(
    viewModel: PalViewModel = hiltViewModel()
) {

    val palList = viewModel.pals

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(palList.entries.toList()) { (key, pal) ->
                    if(viewModel.palNameToKo(key).equals("ko_Text")) {
                        return@items
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                            .border(1.dp, SoftGray, RoundedCornerShape(15.dp))
                            .padding(10.dp)
                            .clickable {

                            }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            viewModel.palData.palPng(
                                name = key,
                                modifier = Modifier
                                    .fillMaxWidth(0.2f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .border(1.dp, SoftGray, CircleShape)
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(text = viewModel.palNameToKo(key))
                        }
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}