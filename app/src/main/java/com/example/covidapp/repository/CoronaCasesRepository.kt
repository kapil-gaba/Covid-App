package com.example.covidapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.covidapp.database.CoronaDataBase
import com.example.covidapp.database.asDomainModelGlobal
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.network.GlobalDataApi
import com.example.covidapp.network.asDatabaseModelGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// repository to fetch data from network and store on local database
class CoronaCasesRepository(private val database : CoronaDataBase){

    val globalCasesDataBase : LiveData<GlobalData> = Transformations.map(database.dataDao.getGlobalData()){
        it?.asDomainModelGlobal()
    }

    suspend fun refreshDatabase(){
        withContext(Dispatchers.IO){
            val globalCasesNetwork = GlobalDataApi.globalDataService.getGlobalData().await()

            val addId = database.dataDao.insertAllGlobalData(*globalCasesNetwork.asDatabaseModelGlobal())
            Log.i("CoronaRepo"," ${addId}")
        }
    }

}