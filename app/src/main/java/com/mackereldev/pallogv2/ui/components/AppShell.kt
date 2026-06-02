package com.mackereldev.pallogv2.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@Composable
fun AppShell(
    navController: NavHostController,
    content: @Composable (onMenuClick: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val onMenuClick: () -> Unit = { scope.launch { drawerState.open() } }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "팰로그",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItem(
                    label = { Text("팰 도감") },
                    selected = currentRoute == "palList",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("palList")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("아이템 도감") },
                    selected = currentRoute == "itemList",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("itemList")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("건축물 도감") },
                    selected = currentRoute == "buildingList",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("buildingList")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("교배") },
                    selected = currentRoute == "breeding",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("breeding")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("기술") },
                    selected = currentRoute == "technology",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("technology")
                    }
                )
//                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("검색") },
                    selected = currentRoute == "search",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("search")
                    }
                )
            }
        }
    ) {
        content(onMenuClick)
    }
}