package com.mackereldev.pallogv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mackereldev.pallogv2.ui.theme.Pallogv2Theme
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.compose.rememberNavController
import com.mackereldev.pallogv2.navigation.NavGraph
import com.mackereldev.pallogv2.ui.components.AppShell

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Pallogv2Theme {
//                var showSplash by remember { mutableStateOf(true) }
//                LaunchedEffect(Unit) {
//                    delay(1000L)
//                    showSplash = false
//                }
                val navController = rememberNavController()
                AppShell(navController = navController,) { onMenuClick ->
                    NavGraph(navController = navController, onMenuClick = onMenuClick)
                }
            }
        }
    }
}