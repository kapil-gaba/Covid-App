package com.example.covidapp.network

import com.example.covidapp.database.DatabaseCountryData
import com.example.covidapp.database.DatabaseCountryInfo
import com.example.covidapp.database.DatabaseGlobalData
import com.example.covidapp.domain.CountriesData
import com.example.covidapp.domain.DataCountryInfo
import com.google.gson.annotations.SerializedName


data class NetworkGlobalData (
    val cases : Long,
    val deaths : Long,
    val recovered : Long
)

data class NetworkCountriesData (
    val country : String,
    @SerializedName("countryInfo")
    val networkCountryInfo : NetworkCountryInfo?,
    val cases : Int,
    val todayCases : Int,
    val deaths : Int,
    val todayDeaths : Int,
    val recovered : Int,
    val active : Int,
    val critical : Int
)

data class NetworkCountryInfo(
    val iso2 : String?,
    val iso3 : String?
)


fun NetworkGlobalData.asDatabaseModelGlobal() : Array<DatabaseGlobalData>{

    return arrayOf(DatabaseGlobalData(1,cases,deaths,recovered))

}

fun List<NetworkCountriesData>.asDatabaseModelCountry() : Array<DatabaseCountryData>{

    return map {
        DatabaseCountryData(
            country = it.country,
            countryInfo = DatabaseCountryInfo(
                iso2 = it.networkCountryInfo?.iso2,
                iso3 =  it.networkCountryInfo?.iso3),
            cases = it.cases,
            todayCases = it.todayCases,
            deaths = it.deaths,
            todayDeaths = it.todayDeaths,
            recovered = it.recovered,
            active = it.active,
            critical = it.critical
        )
    }.toTypedArray()
}


fun List<NetworkCountriesData>.convertToCountriesDataFromNetwork() : List<CountriesData>{

    return map {
        CountriesData(
            country = it.country,
            countryInfo = DataCountryInfo(
                iso2 = it.networkCountryInfo?.iso2,
                iso3 =  it.networkCountryInfo?.iso3),
            cases = it.cases,
            todayCases = it.todayCases,
            deaths = it.deaths,
            todayDeaths = it.todayDeaths,
            recovered = it.recovered,
            active = it.active,
            critical = it.critical
        )
    }
}