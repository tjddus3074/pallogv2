package com.mackereldev.pallogv2.ui.breeding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.breeding.PalBreedingLogic
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.model.UniqueCombo
import com.mackereldev.pallogv2.data.repository.BreedingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class BreedingMode { PARENT_TO_CHILD, CHILD_TO_PARENT, UNIQUE_COMBO }

//화면 표시용 특수 조합(부모1 + 부모2 -> 자식, 성별 조건 포험)
data class UniqueComboUi(
    val parent1: Pal,
    val parent1Gender: String,
    val parent2: Pal,
    val parent2Gender: String,
    val child: Pal
)

data class ParentPairUi(
    val parent1: Pal,
    val parent1Gender: String,
    val parent2: Pal,
    val parent2Gender: String
)

sealed class BreedingUiState{
    object Loading : BreedingUiState()
    data class Success(val pals: List<Pal>, val uniqueCombo: List<UniqueComboUi>): BreedingUiState()
    data class Error(val message: String) : BreedingUiState()
}

@HiltViewModel
class BreedingViewModel @Inject constructor(
    private val repository: BreedingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BreedingUiState>(BreedingUiState.Loading)
    val uiState: StateFlow<BreedingUiState> = _uiState.asStateFlow()

    private val _mode = MutableStateFlow(BreedingMode.PARENT_TO_CHILD)
    val mode: StateFlow<BreedingMode> = _mode.asStateFlow()

    private val _parent1 = MutableStateFlow<Pal?>(null)
    val parent1: StateFlow<Pal?> = _parent1.asStateFlow()

    private val _parent2 = MutableStateFlow<Pal?>(null)
    val parent2: StateFlow<Pal?> = _parent2.asStateFlow()

    private val _child = MutableStateFlow<Pal?>(null)
    val child: StateFlow<Pal?> = _child.asStateFlow()

    private val _material = MutableStateFlow<Pal?>(null)
    val material: StateFlow<Pal?> = _material.asStateFlow()

    private val _comboFilter = MutableStateFlow<Pal?>(null)
    val comboFilter: StateFlow<Pal?> = _comboFilter.asStateFlow()

    //부모 -> 자식 결과(특수 조합이면 여러마리일 수 있음)
    private val _childResults = MutableStateFlow<List<Pal>>(emptyList())
    val childResults: StateFlow<List<Pal>> = _childResults.asStateFlow()

    private val _resultCombos = MutableStateFlow<List<UniqueComboUi>>(emptyList())
    val resultCombos: StateFlow<List<UniqueComboUi>> = _resultCombos.asStateFlow()

    private val _parentPairs = MutableStateFlow<List<ParentPairUi>>(emptyList())
    val parentPairs: StateFlow<List<ParentPairUi>> = _parentPairs.asStateFlow()

    private var logic: PalBreedingLogic? = null
    private var palsByCode: Map<String, Pal> = emptyMap()
    private var allCombos: List<UniqueComboUi> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val breedingLogic = repository.getLogic()
                logic = breedingLogic
                palsByCode = repository.getPalsByCode()
                allCombos = breedingLogic.uniqueCombos.mapNotNull { combo ->
                    val p1 = palsByCode[combo.parent1Code]
                    val p2 = palsByCode[combo.parent2Code]
                    val c = palsByCode[combo.childCode]
                    if (p1 != null && p2 != null && c != null) {
                        UniqueComboUi(p1, combo.parent1Gender, p2, combo.parent2Gender, c)
                    } else null
                }
                repository.getBreedablePals() to allCombos
            }
                .onSuccess { (pals, combos) -> _uiState.value = BreedingUiState.Success(pals, combos) }
                .onFailure { _uiState.value = BreedingUiState.Error(it.message ?: "오류 발생") }
        }
    }

    fun selectMode(mode: BreedingMode) { _mode.value =  mode }

    fun selectParent1(pal: Pal?) { _parent1.value = pal; computeChild() }
    fun selectParent2(pal: Pal?) { _parent2.value = pal; computeChild() }
    fun selectChild(pal: Pal?) { _child.value = pal; computeParentPairs() }
    fun selectMaterial(pal: Pal?) { _material.value = pal; computeParentPairs() }
    fun selectComboFilter(pal: Pal?) { _comboFilter.value = pal }

    fun filteredCombos(): List<UniqueComboUi> {
        val code = _comboFilter.value?.stats?.code ?: return allCombos
        return allCombos.filter {
            it.parent1.stats.code == code || it.parent2.stats.code == code || it.child.stats.code == code
        }
    }

    private fun computeChild() {
        val p1 = _parent1.value
        val p2 = _parent2.value
        if (p1 == null || p2 == null) {
            _childResults.value = emptyList()
            _resultCombos.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val breedingLogic = logic ?: return@launch
            val codes = breedingLogic.getChildren(p1.stats.code, p2.stats.code).keys
            _childResults.value = codes.mapNotNull { palsByCode[it] }
            _resultCombos.value = breedingLogic
                .uniqueCombosForPair(p1.stats.code, p2.stats.code)
                .mapNotNull { combo -> allCombos.find { it.child.stats.code == combo.childCode } }
        }
    }

    private fun computeParentPairs() {
        val target = _child.value
        if (target == null) {
            _parentPairs.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            val breedingLogic = logic ?: return@launch
            val materialCode = _material.value?.stats?.code
            val uniqueEntries = breedingLogic.uniqueCombosForChild(target.stats.code)

            val pairs = if (uniqueEntries.isNotEmpty()) {
                // 특수 조합으로 나오는 자식이면 성별 조건까지 그대로 표시
                uniqueEntries.mapNotNull { combo ->
                    val p1 = palsByCode[combo.parent1Code]
                    val p2 = palsByCode[combo.parent2Code]
                    if (p1 != null && p2 != null) ParentPairUi(p1, combo.parent1Gender, p2, combo.parent2Gender) else null
                }
            } else {
                breedingLogic.getParentPairs(target.stats.code).mapNotNull { (a, b) ->
                    val pa = palsByCode[a.id]
                    val pb = palsByCode[b.id]
                    if (pa != null && pb != null) ParentPairUi(pa, "", pb, "") else null
                }
            }

            _parentPairs.value = pairs.filter { pair ->
                materialCode == null || pair.parent1.stats.code == materialCode || pair.parent2.stats.code == materialCode
            }
        }
    }
}