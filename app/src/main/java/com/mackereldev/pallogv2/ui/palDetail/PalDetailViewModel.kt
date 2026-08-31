package com.mackereldev.pallogv2.ui.palDetail

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

sealed class PalDetailUiState {
    object Loading : PalDetailUiState()
    data class Success(val pal: Pal) : PalDetailUiState()
    data class Error(val message: String) : PalDetailUiState()
}

@HiltViewModel
class PalDetailViewModel @Inject constructor(
    private val repository: PalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PalDetailUiState>(PalDetailUiState.Loading)
    val uiState: StateFlow<PalDetailUiState> = _uiState.asStateFlow()

    fun loadPal(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getPal(code) }
                .onSuccess { pal ->
                    _uiState.value = if (pal != null) PalDetailUiState.Success(pal)
                    else PalDetailUiState.Error("팰을 찾을 수 없습니다.")
                }
                .onFailure { _uiState.value = PalDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }

}