package com.example.covidapp.data.source.local

import android.provider.Settings
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.covidapp.database.CoronaDataBase
import com.example.covidapp.database.DatabaseGlobalData
import com.example.covidapp.getOrAwaitDataValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TestRoomDao {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CoronaDataBase


    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            CoronaDataBase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertDataAndGet()= runBlockingTest {

        val globalData = DatabaseGlobalData(1,1489633, 45666, 745544)
        database.dataDao.insertAllGlobalData(globalData)

        val loaded = database.dataDao.getGlobalData().getOrAwaitDataValue()

        assertThat(loaded.id, `is`(globalData.id))
        assertEquals(loaded.cases, globalData.cases )
    }

    @After
    fun closeDb() = database.close()

}