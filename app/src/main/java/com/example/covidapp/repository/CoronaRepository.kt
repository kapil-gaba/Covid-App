package com.example.covidapp.repository

import androidx.lifecycle.LiveData
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.network.NetworkCountriesData
import io.reactivex.Observable
import io.reactivex.Single

interface CoronaRepository {
    val globalCasesDataBase : LiveData<GlobalData>
    fun globalCasesFromDataBase() : Observable<List<NetworkCountriesData>>
    fun countryCasesDataBase(country : String) : Single<List<NetworkCountriesData>>

    suspend fun refreshGlobalData()
    fun refreshCountryData(countryCasesNetwork : List<NetworkCountriesData>)
    // for testing
    suspend fun clearAllData()
    suspend fun addGlobalData(vararg globalData: GlobalData)

}