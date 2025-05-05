package android.template.ui

import android.template.feature.filters.FiltersScreen
import android.template.feature.movies.MoviesScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "movies"
    ) {
        composable("movies") {
            MoviesScreen(
                modifier = Modifier.padding(0.dp),
                navController = navController
            )
        }
        composable(
            route = "filters?genreId={genreId}",
            arguments = listOf(
                navArgument("genreId") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            FiltersScreen(
                genreId = it.arguments?.getInt("genreId") ?: -1,
                modifier = Modifier.padding(0.dp),
                navController = navController
            )
        }
    }
}
