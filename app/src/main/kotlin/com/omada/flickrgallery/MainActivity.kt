package com.omada.flickrgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.omada.flickrgallery.ui.navigation.AppNavGraph
import com.omada.flickrgallery.ui.theme.OmadaflickrgalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmadaflickrgalleryTheme {
                AppNavGraph()
            }
        }
    }
}
