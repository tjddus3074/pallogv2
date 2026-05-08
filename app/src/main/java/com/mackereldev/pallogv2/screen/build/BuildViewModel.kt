package com.mackereldev.pallogv2.screen.build

import androidx.lifecycle.ViewModel
import com.mackereldev.pallogv2.dataset.buildobject.BuildObjectData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BuildViewModel @Inject constructor(
    val buildData: BuildObjectData
) : ViewModel() {

    val buildObjectList = buildData.buildObjectDataList()
    val buildObjectKo = buildData.buildObjectNameKoList()

    fun buildObjectNameToKo(name: String) : String {
        return buildObjectKo["MAPOBJECT_NAME_${name}"] as? String ?: name
    }
}