package android.template.feature.filters

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
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

    lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        navController = TestNavHostController(composeTestRule.activity)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        composeTestRule.setContent {
            FiltersScreen(
                genreId = 1,
                navController = navController
            )
        }


    }

//    @Test
//    fun firstItem_exists() {
//        composeTestRule.onNodeWithText(FAKE_DATA.first()).assertExists().performClick()
//    }
}

//private val FAKE_DATA = listOf("Compose", "Room", "Kotlin")
