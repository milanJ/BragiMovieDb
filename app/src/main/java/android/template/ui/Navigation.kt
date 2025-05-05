package android.template.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.template.feature.movies.MoviesScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "movies"
    ) {
        composable("movies") { MoviesScreen(modifier = Modifier.padding(16.dp)) }
        // TODO: Add more destinations
    }
}
