package com.mackereldev.pallogv2.ui.weaponDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackereldev.pallogv2.data.model.AmmoItem
import com.mackereldev.pallogv2.data.model.WeaponItem
import com.mackereldev.pallogv2.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class WeaponDetailUiState{
    object Loading : WeaponDetailUiState()
    data class Success(val weapon: WeaponItem, val ammo: AmmoItem?, val materialIcons: Map<String, String>) : WeaponDetailUiState()
    data class Error(val message: String) : WeaponDetailUiState()
}

// 무기 TypeB -> 사용 탄약 Code 추정 매핑.
// JSON에 무기-탄약 직접 연결 필드가 없어 이름 기준으로 추측한 값이라 실제 게임 데이터로 검증 필요.
private val weaponAmmoCodeByTypeB = mapOf(
    "WeaponBow" to "Arrow",
    "WeaponCrossbow" to "Arrow",
    "WeaponHandgun" to "HandgunBullet",
    "WeaponShotgun" to "ShotgunBullet",
    "WeaponAssaultRifle" to "AssaultRifleBullet",
    "WeaponRocketLauncher" to "MissileBullet",
    "WeaponGatlingGun" to "GatlingBullet",
    "WeaponFlameThrower" to "FlamethrowerBullet"
)

@HiltViewModel
class WeaponDetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeaponDetailUiState>(WeaponDetailUiState.Loading)
    val uiState: StateFlow<WeaponDetailUiState> = _uiState.asStateFlow()

    fun loadWeapon(href: String) {
        _uiState.value = WeaponDetailUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.getWeapon(href) }
                .onSuccess { weapon ->
                    if(weapon == null) {
                        _uiState.value = WeaponDetailUiState.Error("무기를 찾을 수 없습니다.")
                        return@onSuccess
                    }
                    val typeB = weapon.levels.firstOrNull()?.typeB.orEmpty()
                    val ammoCode = weaponAmmoCodeByTypeB[typeB]
                    val ammo = ammoCode?.let { repository.getAmmoByCode(it) }

                    val materialNames = weapon.effectiveLevels.flatMap { it.meterial.keys }.distinct()
                    val materialIcons = materialNames.mapNotNull { name ->
                        repository.getMaterialIconPath(name)?.let { name to it }
                    }.toMap()
                    _uiState.value = WeaponDetailUiState.Success(weapon, ammo, materialIcons)
                }
                .onFailure { _uiState.value = WeaponDetailUiState.Error(it.message ?: "오류 발생") }
        }
    }
}