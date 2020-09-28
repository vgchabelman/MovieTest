package br.eloibrito.com.movie_db

import android.app.Activity
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ActivityListMovieEspressoTest {

    @Rule
    @JvmField
    var arules: ActivityTestRule<ActivityListMovies> =
        ActivityTestRule(ActivityListMovies::class.java, false, true)

    private var launchedActivity: Activity? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra("parameter", "Value")
        launchedActivity = arules.launchActivity(intent)

    }

    @After
    @Throws(Exception::class)
    fun down() {

    }


    @Test
    fun apiCall() {

            Espresso.onView(ViewMatchers.withText(arules.activity.viewModel._isMessageError.value))
                .check(
                    ViewAssertions.matches(
                        ViewMatchers.withText("Erro ao baixar os generos.")
                    )
                )

            Espresso.onView(ViewMatchers.withText(arules.activity.viewModel._isMessageError.value))
                .check(
                    ViewAssertions.matches(
                        ViewMatchers.withText("Erro ao baixar os filmes.")
                    )
                )



    }

}