package com.sukumar.et

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.sukumar.et.ETData.ETViewModel
import com.sukumar.et.ui.theme.SetUpNavGraphs
import org.koin.androidx.viewmodel.ext.android.getViewModel
import androidx.core.os.BuildCompat
import com.sukumar.et.ui.theme.ETTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val vm :ETViewModel = getViewModel()
        if (BuildCompat.isAtLeastQ()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContent {
            ETTheme {
//                 A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFBFA)
                ) {


                    val navController = rememberNavController()
                    SetUpNavGraphs(
                        navController = navController,
                        viewm = vm
                    )


                }
            }
        }
    }
}

 val LightColorPalette = lightColorScheme(
    primary = Color(0xFF007BFF), // Bright blue
    secondary = Color(0xFF28A745), // Green
    background = Color(0xFFF0F0F0), // Light gray
    surface = Color(0xFFFFFFFF), // White
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF333333), // Dark gray
    onSurface = Color(0xFF333333), // Dark gray
)


