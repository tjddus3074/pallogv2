package com.mackereldev.pallogv2.screen.item.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mackereldev.pallogv2.screen.item.ItemViewModel
import com.mackereldev.pallogv2.ui.theme.SoftGray

@Composable
fun ItemListView(
    viewModel: ItemViewModel = hiltViewModel()
) {

    val itemList = viewModel.items

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(itemList.entries.toList(), key = { (key, _) -> key }) { (key, item) ->
                    if(viewModel.itemNameToKo(key).equals("ko_Text")) {
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
                                //TODO move to detail page
                            }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            viewModel.itemData.itemPng(
                                name = key,
                                modifier = Modifier
                                    .fillMaxWidth(0.2f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .border(1.dp, SoftGray, CircleShape)
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(text = viewModel.itemNameToKo(key))
                        }
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}