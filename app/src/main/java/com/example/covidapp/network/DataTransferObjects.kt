package com.example.covidapp.network

import com.example.covidapp.database.DatabaseGlobalData


data class NetworkGlobalData (
    val cases : Long,
    val deaths : Long,
    val recovered : Long
)

data class NetworkCountriesData (
    val country : String,
    val cases : Int,
    val todayCases : Int,
    val deaths : Int,
    val todayDeaths : Int,
    val recovered : Int,
    val active : Int,
    val critical : Int
)



fun NetworkGlobalData.asDatabaseModelGlobal() : Array<DatabaseGlobalData>{

    return arrayOf(DatabaseGlobalData(1,cases,deaths,recovered))

}