package com.mackereldev.pallogv2.ui.palList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.repository.PalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PalListUiState {
    object Loading : PalListUiState()
    data class Success(val pals: List<Pal>) : PalListUiState()
    data class Error(val message: String) : PalListUiState()
}

@HiltViewModel
class PalListViewModel @Inject constructor(
    private val repository: PalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PalListUiState>(PalListUiState.Loading)
    val uiState: StateFlow<PalListUiState> = _uiState.asStateFlow()

    init { loadPals() }

    private fun loadPals() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getPals() }
                .onSuccess { _uiState.value = PalListUiState.Success(it) }
                .onFailure { _uiState.value = PalListUiState.Error(it.message ?: "오류 발생") }
        }
    }
}