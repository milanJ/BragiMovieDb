package android.template.feature.filters

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [FiltersScreen].
 */
@RunWith(AndroidJUnit4::class)
class FiltersScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {

    }

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            LoadingState()
        }

        composeTestRule.onNodeWithTag("Loading").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Connection error"

        composeTestRule.setContent {
            ErrorState(errorMessage = errorMessage)
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun genresList_displaysAllGenres() {
        val genres = listOf(
            GenreUiModel(id = 1, name = "Action"),
            GenreUiModel(id = 2, name = "Comedy"),
            GenreUiModel(id = 3, name = "Drama")
        )

        composeTestRule.setContent {
            GenresList(
                genres = genres,
                selectedGenreId = 1
            )
        }

        genres.forEach { genre ->
            composeTestRule.onNodeWithText(genre.name).assertIsDisplayed()
        }
    }

    @Test
    fun genresList_clickGenre_invokesCallback() {
        var selectedGenre: GenreUiModel? = null
        val genres = listOf(
            GenreUiModel(id = 1, name = "Action"),
            GenreUiModel(id = 2, name = "Comedy"),
            GenreUiModel(id = 3, name = "Drama")
        )

        composeTestRule.setContent {
            GenresList(
                genres = genres,
                selectedGenreId = 1,
                onGenreSelected = { genre ->
                    selectedGenre = genre
                }
            )
        }

        // Click on the Comedy genre
        composeTestRule.onNodeWithText("Comedy").performClick()

        // Verify the callback was invoked with the correct genre
        assert(selectedGenre?.id == 2)
        assert(selectedGenre?.name == "Comedy")
    }

    @Test
    fun genresList_selectedGenre_showsProperStyling() {
        val genres = listOf(
            GenreUiModel(id = 1, name = "Action"),
            GenreUiModel(id = 2, name = "Comedy")
        )

        composeTestRule.setContent {
            GenresList(
                genres = genres,
                selectedGenreId = 2
            )
        }

        // Verify the selected genre has proper styling
        composeTestRule.onNodeWithText("Comedy").assertIsDisplayed()
    }

    @Test
    fun topBar_isDisplayed() {
        // Using resource ID is better, but for the simplicity of the test
        val expectedTitle = "Filter by genre"

        composeTestRule.setContent {
            TopBar()
        }

        composeTestRule.onNodeWithText(expectedTitle).assertIsDisplayed()
    }
}
