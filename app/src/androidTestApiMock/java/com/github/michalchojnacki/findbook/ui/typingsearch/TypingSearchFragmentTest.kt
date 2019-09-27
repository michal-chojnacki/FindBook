package com.github.michalchojnacki.findbook.ui.typingsearch

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.ui.navigation.MainNavigationViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TypingSearchFragmentTest {
    private lateinit var mainNavigationViewModel: MainNavigationViewModel

    @Test
    fun testInsertingQuery_WhenNotEmpty() {
        val fakeQuery = "fake query"

        launchFragmentInContainer<TypingSearchFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }

        Espresso.onView(withId(R.id.typingSearchWithTextEt))
            .perform(ViewActions.typeText(fakeQuery))
        Espresso.onView(withId(R.id.typingSearchWithTextBtn)).perform(ViewActions.click())
        Assert.assertEquals(
            MainNavigationViewModel.UiResult.ShowBookList(fakeQuery),
            mainNavigationViewModel.uiResultLiveData.value!!.consume()
        )
    }

    @Test
    fun testInsertingQuery_WhenEmpty() {
        launchFragmentInContainer<TypingSearchFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }

        Espresso.onView(withId(R.id.typingSearchWithTextBtn)).perform(ViewActions.click())
        Assert.assertEquals(null, mainNavigationViewModel.uiResultLiveData.value!!.consume())
    }

    @Test
    fun testGoingToOcrScanner() {
        launchFragmentInContainer<TypingSearchFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }

        Espresso.onView(withId(R.id.typingSearchWithOcrBtn)).perform(ViewActions.click())
        Assert.assertEquals(
            MainNavigationViewModel.UiResult.ShowOcrScanner,
            mainNavigationViewModel.uiResultLiveData.value!!.consume()
        )
    }
}