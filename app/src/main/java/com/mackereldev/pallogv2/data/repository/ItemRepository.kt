package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.model.ItemCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(
    private val dataSource: AssetDataSource
) {
    private val cache = mutableMapOf<ItemCategory, List<Item>>()

    fun getItems(category: ItemCategory): List<Item> {
        return cache.getOrPut(category) {
            dataSource.loadItems(category.fileName)
        }
    }
}