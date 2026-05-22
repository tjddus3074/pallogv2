package com.mackereldev.pallogv2.ui.palList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackereldev.pallogv2.ui.components.PalCard

private const val COLUMN_COUNT = 3
@Composable
fun PalListView(
    uiState: PalListUiState,
    onPalClick: (Int) -> Unit
) {

    val background = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)

    when(uiState) {
        is PalListUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is PalListUiState.Error -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text(uiState.message) }

        is PalListUiState.Success -> {
            val rows = uiState.pals.chunked(COLUMN_COUNT)
            LazyColumn(modifier = background) {
                items(rows) { rowPals ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        rowPals.forEach { pal ->
                            PalCard(
                                pal = pal,
                                onClick = { onPalClick(pal.palDeckIndex) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(COLUMN_COUNT - rowPals.size) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}