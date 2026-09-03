package com.mackereldev.pallogv2.ui.tech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.TechRow
import com.mackereldev.pallogv2.data.repository.TechNavTarget
import com.mackereldev.pallogv2.data.repository.TechRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TechUiState {
    object Loading : TechUiState()
    data class Success(val rows: List<TechRow>) : TechUiState()
    data class Error(val message: String) : TechUiState()
}

@HiltViewModel
class TechViewModel @Inject constructor(
    private val repository: TechRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TechUiState>(TechUiState.Loading)
    val uiState: StateFlow<TechUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getTechRows() }
                .onSuccess { _uiState.value = TechUiState.Success(it) }
                .onFailure { _uiState.value = TechUiState.Error(it.message ?: "오류 발생") }
        }
    }

    fun resolveTarget(itemName: String): TechNavTarget? = repository.resolveTarget(itemName)
}