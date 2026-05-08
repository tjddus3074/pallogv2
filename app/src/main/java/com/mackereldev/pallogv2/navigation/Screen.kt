package com.mackereldev.pallogv2.navigation

sealed class Screen(val route: String) {
    object PalList   : Screen("pal_list")
    object ItemList  : Screen("item_list")
    object BuildList : Screen("build_list")
    object Breeding  : Screen("breeding")
    object Tech     : Screen("tech")
    object Search    : Screen("search")
}