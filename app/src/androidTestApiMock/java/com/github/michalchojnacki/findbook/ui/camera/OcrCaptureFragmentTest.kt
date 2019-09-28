package com.github.michalchojnacki.findbook.ui.camera

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.viewModels
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.runner.permission.PermissionRequester
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.data.di.MockSearchForBooksService
import com.github.michalchojnacki.findbook.ui.di.InjectorProvider
import com.github.michalchojnacki.findbook.ui.helpers.WaitPeriod
import com.github.michalchojnacki.findbook.ui.helpers.wait
import com.github.michalchojnacki.findbook.ui.navigation.MainNavigationViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class OcrCaptureFragmentTest {
    private val mockSearchForBooksService =
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as InjectorProvider).component.searchForBooksService as MockSearchForBooksService
    private lateinit var viewModel: OcrCaptureViewModel
    private lateinit var mainNavigationViewModel: MainNavigationViewModel

    @Before
    fun setUp() {
        clearAllMocks()
        PermissionRequester().apply {
            addPermissions(android.Manifest.permission.CAMERA)
            requestPermissions()
        }
    }

    @Test
    fun testScanning_WhenTextHasBooks() {
        val fakeText = "fakeText1"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(fakeText) }.returns(
            Response.success(mockSearchForBooksService.successfulResponseBody)
        )

        launchFragmentInContainer<OcrCaptureFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            viewModel = it.viewModels<OcrCaptureViewModel>().value
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }
        wait(WaitPeriod.SHORT)
        viewModel.onTextDetected(fakeText)

        wait(WaitPeriod.SHORT)
        coVerify(exactly = 1) {
            mockSearchForBooksService.searchForBooksWithQuery(any())
            mockSearchForBooksService.searchForBooksWithQuery(fakeText)
        }
        Assert.assertEquals(
            MainNavigationViewModel.UiResult.ShowBookList(fakeText),
            mainNavigationViewModel.uiResultLiveData.value!!.consume()
        )
    }


    @Test
    fun testScanning_WhenTextDoesNotHaveBooks() {
        val fakeText = "fakeText2"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(fakeText) }.returns(
            Response.success(mockSearchForBooksService.emptyResponseBody)
        )

        launchFragmentInContainer<OcrCaptureFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            viewModel = it.viewModels<OcrCaptureViewModel>().value
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }
        wait(WaitPeriod.SHORT)
        viewModel.onTextDetected(fakeText)

        wait(WaitPeriod.SHORT)
        coVerify(exactly = 1) {
            mockSearchForBooksService.searchForBooksWithQuery(any())
            mockSearchForBooksService.searchForBooksWithQuery(fakeText)
        }
        Assert.assertEquals(null, mainNavigationViewModel.uiResultLiveData.value!!.consume())
    }

    @Test
    fun testScanning_WhenQueryIsTooShort() {
        val fakeText = "a"

        launchFragmentInContainer<OcrCaptureFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            viewModel = it.viewModels<OcrCaptureViewModel>().value
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }
        wait(WaitPeriod.SHORT)
        viewModel.onTextDetected(fakeText)

        wait(WaitPeriod.SHORT)
        coVerify(exactly = 0) {
            mockSearchForBooksService.searchForBooksWithQuery(any())
        }
        Assert.assertEquals(null, mainNavigationViewModel.uiResultLiveData.value!!.consume())
    }

    @Test
    fun testScanning_WhenThereWasConnectionError() {
        val fakeText = "fakeText4"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(fakeText) }.answers {
            throw Exception("Fake exception!")
        }

        launchFragmentInContainer<OcrCaptureFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            viewModel = it.viewModels<OcrCaptureViewModel>().value
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }
        wait(WaitPeriod.SHORT)
        viewModel.onTextDetected(fakeText)

        wait(WaitPeriod.SHORT)
        coVerify(exactly = 1) {
            mockSearchForBooksService.searchForBooksWithQuery(any())
            mockSearchForBooksService.searchForBooksWithQuery(fakeText)
        }
        Assert.assertEquals(null, mainNavigationViewModel.uiResultLiveData.value!!.consume())
    }

    @Test
    fun testClickingGoToTyping() {
        launchFragmentInContainer<OcrCaptureFragment>(
            themeResId = R.style.AppTheme
        ).onFragment {
            viewModel = it.viewModels<OcrCaptureViewModel>().value
            mainNavigationViewModel = it.activityViewModels<MainNavigationViewModel>().value
            mainNavigationViewModel.uiResultLiveData.value?.consume()
        }
        wait(WaitPeriod.SHORT)

        Espresso.onView(ViewMatchers.withId(R.id.scannerGoToSearchTextBtn))
            .perform(ViewActions.click())

        wait(WaitPeriod.SHORT)
        Assert.assertEquals(
            MainNavigationViewModel.UiResult.ShowTypingSearch(false),
            mainNavigationViewModel.uiResultLiveData.value!!.consume()
        )
    }
}