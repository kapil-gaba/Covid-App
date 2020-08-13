package com.example.covidapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.covidapp.data.source.FakeTestRepository
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.getOrAwaitValue
import com.example.covidapp.repository.CoronaRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlobalViewModelTest{

    private lateinit var globalViewModel: GlobalViewModel

    private lateinit var coronaRepository: FakeTestRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){
        coronaRepository = FakeTestRepository()
        val globalData = GlobalData(145555, 48855, 44555)
        runBlocking{
            coronaRepository.addGlobalData(globalData)
        }


        globalViewModel = GlobalViewModel(coronaRepository)

    }

    @Test
    fun addGlobalData_checkGlobalData(){
     val value = globalViewModel.globalCases.getOrAwaitValue()
        assertEquals(value.cases, 145555)
    }

}