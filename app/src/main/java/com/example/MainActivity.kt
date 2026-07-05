package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.navigation.AppNavigation
import com.example.presentation.viewmodel.AppViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Fully edge-to-edge immersive design
        enableEdgeToEdge()

        setContent {
            // Dynamic theme controller observing the preference state in our viewmodel
            val darkModeVal by viewModel.darkModeFlow.collectAsStateWithLifecycle()
            val isSystemDark = isSystemInDarkTheme()
            val activeDark = darkModeVal ?: isSystemDark

            MyApplicationTheme(darkTheme = activeDark) {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
