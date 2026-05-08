package com.mackereldev.pallogv2.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mackereldev.pallogv2.navigation.Screen
import com.mackereldev.pallogv2.screen.breeding.BreedingScreen
import com.mackereldev.pallogv2.screen.build.BuildListScreen
import com.mackereldev.pallogv2.screen.item.ItemListScreen
import com.mackereldev.pallogv2.screen.pal.PalListScreen
import com.mackereldev.pallogv2.screen.search.SearchScreen
import com.mackereldev.pallogv2.screen.tech.TechScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: Screen.PalList.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("PalLog") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
                    }
                }
            )
        }
    ) { innerPadding ->
        ModalNavigationDrawer(
            modifier = Modifier.padding(innerPadding),
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    onMenuClick = { screen ->
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.PalList.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.PalList.route)   { PalListScreen() }
                composable(Screen.ItemList.route)  { ItemListScreen() }
                composable(Screen.BuildList.route) { BuildListScreen() }
                composable(Screen.Breeding.route)  { BreedingScreen() }
                composable(Screen.Tech.route)     { TechScreen() }
                composable(Screen.Search.route)    { SearchScreen() }
            }
        }
    }
}