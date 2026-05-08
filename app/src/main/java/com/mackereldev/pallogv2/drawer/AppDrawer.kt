package com.mackereldev.pallogv2.drawer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mackereldev.pallogv2.R
import com.mackereldev.pallogv2.navigation.Screen

private val menuItems = listOf(
    Triple("팰도감",    Screen.PalList,   R.drawable.drawericon1),
    Triple("아이템도감", Screen.ItemList,  R.drawable.drawericon2),
    Triple("건축물도감", Screen.BuildList, R.drawable.drawericon3),
    Triple("교배",     Screen.Breeding,  R.drawable.drawericon4),
    Triple("기술",     Screen.Tech,      R.drawable.drawericon5),
    Triple("검색",     Screen.Search,    R.drawable.drawericon6),
)

@Composable
fun AppDrawer(
    currentRoute: String,
    onMenuClick: (Screen) -> Unit
) {
    ModalDrawerSheet(windowInsets = WindowInsets(0)) {
        Spacer(modifier = Modifier.height(16.dp))
        menuItems.forEach { (label, screen, iconRes) ->
            NavigationDrawerItem(
                label = { Text(label) },
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = currentRoute == screen.route,
                onClick = { onMenuClick(screen) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}