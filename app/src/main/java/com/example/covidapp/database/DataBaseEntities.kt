package com.example.covidapp.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.covidapp.domain.CountriesData
import com.example.covidapp.domain.DataCountryInfo
import com.example.covidapp.domain.GlobalData
import com.example.covidapp.network.NetworkCountryInfo
import com.example.covidapp.network.NetworkCountriesData


@Entity
data class DatabaseGlobalData (
    @PrimaryKey
    val id : Int,
    val cases : Long,
    val deaths : Long,
    val recovered : Long
)

@Entity
data class DatabaseCountryData (
    @PrimaryKey
    val country : String,
    @Embedded val countryInfo : DatabaseCountryInfo?,
    val cases : Int,
    val todayCases : Int,
    val deaths : Int,
    val todayDeaths : Int,
    val recovered : Int,
    val active : Int,
    val critical : Int
)

data class DatabaseCountryInfo(
    val iso2 : String?,
    val iso3 : String?
)

fun DatabaseGlobalData.asDomainModelGlobal() : GlobalData{
    return GlobalData(
            cases,deaths,recovered
        )
}

fun List<DatabaseCountryData>.asDatabaseModelCountry() : List<NetworkCountriesData>{

    return map {
        NetworkCountriesData(
            country = it.country,
            networkCountryInfo = NetworkCountryInfo(
                iso2 = it.countryInfo?.iso2,
                iso3 =  it.countryInfo?.iso3),
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


