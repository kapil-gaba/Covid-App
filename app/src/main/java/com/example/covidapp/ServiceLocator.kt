package com.example.covidapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.covidapp.database.CoronaDataBase
import com.example.covidapp.repository.CoronaCasesRepository
import com.example.covidapp.repository.CoronaRepository
import kotlinx.coroutines.runBlocking


object ServiceLocator {

    private var database: CoronaDataBase? = null
    @Volatile
    var coronaRepository: CoronaRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                coronaRepository?.clearAllData()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            coronaRepository = null
        }
    }

    fun provideCoronaRepository(context: Context): CoronaRepository {
        synchronized(this) {
            return coronaRepository ?: createCoronaRepository(context)
        }
    }

    private fun createCoronaRepository(context: Context): CoronaRepository {

        val newRepo = CoronaCasesRepository(createDataBase(context))
        coronaRepository = newRepo
        return newRepo
    }

    private fun createDataBase(context: Context): CoronaDataBase {

            val result = Room.databaseBuilder(
                context.applicationContext,
                CoronaDataBase::class.java, "CoronaCases"
            ).build()
            database = result
            return result
    }

}