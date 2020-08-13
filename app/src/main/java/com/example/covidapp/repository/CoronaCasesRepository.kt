package com.example.covidapp.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.covidapp.database.*
import com.example.covidapp.domain.CountriesData
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.network.GlobalDataApi
import com.example.covidapp.network.NetworkCountriesData
import com.example.covidapp.network.asDatabaseModelCountry
import com.example.covidapp.network.asDatabaseModelGlobal
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// repository to fetch data from network and store on local database
class CoronaCasesRepository(private val database : CoronaDataBase) : CoronaRepository {

    override val globalCasesDataBase : LiveData<GlobalData> = Transformations.map(database.dataDao.getGlobalData()){
        it?.asDomainModelGlobal()
    }

    override fun globalCasesFromDataBase() : Observable<List<NetworkCountriesData>> =
        database.dataDao.getCountryData().map {
                it.asDatabaseModelCountry()
            }.toObservable().doOnError { throwable ->

                Log.i("CoronaRepo", throwable.message)
            }.onErrorReturn { throwable -> ArrayList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override fun countryCasesDataBase(country : String) : Single<List<NetworkCountriesData>> =

        if(country == ""){
            database.dataDao.getCountryData().map {
                it.asDatabaseModelCountry()
            }.doOnError { throwable ->

                Log.i("CoronaRepo", throwable.message)
            }.onErrorReturn { throwable -> ArrayList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }else {

            database.dataDao.getSingleCountryData(country).map {
                it.asDatabaseModelCountry()
            }.doOnError { throwable ->

                Log.i("CoronaRepo", throwable.message)
            }.onErrorReturn { throwable -> ArrayList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        }

    override suspend fun refreshGlobalData(){
        withContext(Dispatchers.IO){
            val globalCasesNetwork = GlobalDataApi.globalDataService.getGlobalData().await()

            val addId = database.dataDao.insertAllGlobalData(*globalCasesNetwork.asDatabaseModelGlobal())
            Log.i("CoronaRepo"," ${addId}")
        }
    }

    override suspend fun clearAllData() {
        TODO("Not yet implemented")
    }

    override suspend fun addGlobalData(vararg globalData: GlobalData) {
        TODO("Not yet implemented")
    }

    override fun refreshCountryData(countryCasesNetwork : List<NetworkCountriesData>){
         database.dataDao.insertAllCountryData(*countryCasesNetwork.asDatabaseModelCountry())
    }

}