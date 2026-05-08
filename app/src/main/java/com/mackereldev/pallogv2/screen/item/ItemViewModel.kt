package com.mackereldev.pallogv2.screen.item

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.mackereldev.pallogv2.dataset.item.ItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    val itemData: ItemData
) : ViewModel() {

    val items = itemData.itemList()
    val itemsKo = itemData.itemDataKoList()

    fun itemNameToKo(name: String) : String {
        return itemsKo["ITEM_NAME_${name}"] as? String ?: name
    }

}