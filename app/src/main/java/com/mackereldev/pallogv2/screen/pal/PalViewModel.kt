package com.mackereldev.pallogv2.screen.pal

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.mackereldev.pallogv2.dataset.pal.PalData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PalViewModel @Inject constructor(
    val palData: PalData
) : ViewModel() {
    val pals = palData.palDataList()
    val palsKo = palData.palDataKoList()

    fun palNameToKo(name: String) : String {
        return palsKo["pal_name_${name.lowercase()}"] as? String ?: name
    }

}