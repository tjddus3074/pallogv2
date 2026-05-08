package com.mackereldev.pallogv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mackereldev.pallogv2.dataset.GetData
import com.mackereldev.pallogv2.dataset.pal.PalData
import com.mackereldev.pallogv2.drawer.MainScreen
import com.mackereldev.pallogv2.ui.theme.Pallogv2Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mackereldev.pallogv2.screen.splash.SplashScreen
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var getdata: GetData
    @Inject lateinit var palData: PalData


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Pallogv2Theme {
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(1000L)
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pallogv2Theme {
        Greeting("Android")
    }
}