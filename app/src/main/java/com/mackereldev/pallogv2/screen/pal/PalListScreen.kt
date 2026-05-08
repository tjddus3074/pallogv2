package com.mackereldev.pallogv2.screen.pal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mackereldev.pallogv2.dataset.pal.PalData
import com.mackereldev.pallogv2.screen.pal.components.PalListView
import javax.inject.Inject

@Composable
fun PalListScreen() {

    PalListView()

//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Text("팰도감")
//        Column(modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center) {
//            Row(
//
//
//            ) { }
//        }
//    }
}
