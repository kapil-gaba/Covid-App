package com.example.covidapp.data.source

import androidx.lifecycle.MutableLiveData
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.network.NetworkCountriesData
import com.example.covidapp.repository.CoronaRepository
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.runBlocking

class FakeAndroidTestRepository : CoronaRepository{

    var globalServiceData: LinkedHashMap<Int, GlobalData> = LinkedHashMap()

    override val globalCasesDataBase = MutableLiveData<GlobalData>()

    override suspend fun refreshGlobalData() {
        globalCasesDataBase.value = globalServiceData.values.toList()[0]
    }

    override suspend fun clearAllData(){
        globalServiceData.clear()
    }

    override suspend fun addGlobalData(vararg globalData: GlobalData) {
        for (globaldata in globalData) {
            globalServiceData[0] = globaldata
        }
         //refreshGlobalData()
    }

    override fun globalCasesFromDataBase(): Observable<List<NetworkCountriesData>> {
        TODO("Not yet implemented")
    }

    override fun countryCasesDataBase(country: String): Single<List<NetworkCountriesData>> {
        TODO("Not yet implemented")
    }

    override fun refreshCountryData(countryCasesNetwork: List<NetworkCountriesData>) {
        TODO("Not yet implemented")
    }
}