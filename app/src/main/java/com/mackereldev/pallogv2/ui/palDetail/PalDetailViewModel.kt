package com.mackereldev.pallogv2.ui.palDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.repository.ItemRepository
import com.mackereldev.pallogv2.data.repository.PalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PalDetailUiState {
    object Loading : PalDetailUiState()
    data class Success(val pal: Pal, val dropIcons: Map<String, String>, val dropLocations: Map<String, Pair<ItemCategory, String>>) : PalDetailUiState()
    data class Error(val message: String) : PalDetailUiState()
}

@HiltViewModel
class PalDetailViewModel @Inject constructor(
    private val repository: PalRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PalDetailUiState>(PalDetailUiState.Loading)
    val uiState: StateFlow<PalDetailUiState> = _uiState.asStateFlow()

    fun loadPal(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getPal(code) }
                .onSuccess { pal ->
                    if (pal == null) {
                        _uiState.value = PalDetailUiState.Error("팰을 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val dropNames = (pal.drops + pal.alphaDrops + pal.worldTreeDrops)
                        .map { it.dropitem }
                        .distinct()
                    val dropIcons = dropNames.mapNotNull { name ->
                        itemRepository.getItemIconpath(name)?.let { name to it }
                    }.toMap()
                    val dropLocations = dropNames.mapNotNull { name ->
                        itemRepository.findItemLocation(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = PalDetailUiState.Success(pal, dropIcons, dropLocations)
                }
                .onFailure { _uiState.value = PalDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}