package com.omada.flickrgallery.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omada.flickrgallery.domain.model.Photo
import com.omada.flickrgallery.ui.detail.DetailScreen
import com.omada.flickrgallery.ui.home.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToDetail = { photo: Photo ->
                    val encodedTitle = Uri.encode(photo.title)
                    val encodedUrl = Uri.encode(photo.imageUrl)

                    // pass id, title, and imageUrl in the route
                    navController.navigate(
                        "detail/${photo.id}/$encodedTitle/$encodedUrl"
                    )
                }
            )
        }

        composable(
            route = "detail/{photoId}/{title}/{imageUrl}",
            arguments = listOf(
                navArgument("photoId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title").orEmpty()
            val imageUrl = backStackEntry.arguments?.getString("imageUrl").orEmpty()

            DetailScreen(
                titleString = title,
                imageUrl = imageUrl,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
