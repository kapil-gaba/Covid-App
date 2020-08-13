package com.example.covidapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface DataDao {

    @Query("select * from databaseglobaldata")
    fun getGlobalData(): LiveData<DatabaseGlobalData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGlobalData(vararg databaseGlobalData: DatabaseGlobalData): List<Long>

    @Query("select * from databasecountrydata")
    fun getCountryData(): Single<List<DatabaseCountryData>>

    @Query("select * from databasecountrydata where iso2 like :country or iso3 like :country or country like :country ")
    fun getSingleCountryData(country: String): Single<List<DatabaseCountryData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCountryData(vararg databaseCountryData: DatabaseCountryData): List<Long>

}

@Database(entities = [DatabaseGlobalData::class, DatabaseCountryData::class], version = 1)
abstract class CoronaDataBase : RoomDatabase() {
    abstract val dataDao: DataDao
}

//private lateinit var INSTANCE: CoronaDataBase
//
//fun getCoronaDataBase(context: Context): CoronaDataBase {
//    synchronized(CoronaDataBase::class.java) {
//        if (!::INSTANCE.isInitialized) {
//            INSTANCE = Room.databaseBuilder(
//                context.applicationContext,
//                CoronaDataBase::class.java, "CoronaCases"
//            ).build()
//        }
//    }
//    return INSTANCE
//
//}



