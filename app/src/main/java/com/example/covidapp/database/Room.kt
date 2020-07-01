package com.example.covidapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*



@Dao
interface DataDao{

    @Query("select * from databaseglobaldata")
    fun getGlobalData() : LiveData<DatabaseGlobalData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGlobalData(vararg databaseGlobalData: DatabaseGlobalData):List<Long>

}

@Database(entities = [DatabaseGlobalData::class],version = 1)
abstract class CoronaDataBase : RoomDatabase(){
    abstract val dataDao : DataDao
}

private lateinit var INSTANCE :CoronaDataBase

fun getCoronaDataBase(context: Context) : CoronaDataBase{
    synchronized(CoronaDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                CoronaDataBase::class.java, "CoronaCases").build()
        }
    }
    return INSTANCE

}
