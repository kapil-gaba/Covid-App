package com.example.covidapp.domain


data class GlobalData (
    val cases : Long,
    val deaths : Long,
    val recovered : Long
)

data class CountriesData (
    val country : String,
    val countryInfo : DataCountryInfo?,
    val cases : Int,
    val todayCases : Int,
    val deaths : Int,
    val todayDeaths : Int,
    val recovered : Int,
    val active : Int,
    val critical : Int
)

data class DataCountryInfo(
    val iso2 : String?,
    val iso3 : String?
)