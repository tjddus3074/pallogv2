package com.mackereldev.pallogv2.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mackereldev.pallogv2.R
import kotlinx.coroutines.launch

@Composable
fun AppShell(
    navController: NavHostController,
    content: @Composable (onMenuClick: () -> Unit) -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val onMenuClick: () -> Unit = { scope.launch { drawerState.open() }}

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
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
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.drawericon1),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            selected = currentRoute == "palList",
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("palList")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("아이템 도감") },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.drawericon2),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            selected = currentRoute == "itemList",
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("itemList")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("건축물 도감") },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.drawericon4),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            selected = currentRoute == "buildingList",
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("buildingList")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("교배") },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.drawericon3),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            selected = currentRoute == "breeding",
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("breeding")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("기술") },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.drawericon5),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            selected = currentRoute == "technology",
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("technology")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("검색") },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.drawericon6),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            selected = currentRoute == "search",
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("search")
                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                    ) {
                        HorizontalDivider()
                        Text(
                            text = "This app is fan-made and is not affiliated with or endorsed by Pocketpair.",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }
            }
        }

) {
        content(onMenuClick)
    }
}