package com.example.covidapp.ui


import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.covidapp.R
import com.example.covidapp.ServiceLocator
import com.example.covidapp.data.source.FakeAndroidTestRepository
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.repository.CoronaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class GlobalFragmentTest{

    private lateinit var repository: CoronaRepository


    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.coronaRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun globalData_DisplayUi() = runBlockingTest{

        val globalData = GlobalData(1254896, 48855, 44555)
        repository.addGlobalData(globalData)

        launchFragmentInContainer<GlobalFragment>(null, R.style.AppTheme)
        onView(withId(R.id.numbersTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.numbersTextView)).check(matches(withText("1254896")))
        Thread.sleep(6000)
    }
}