package com.example.covidapp.network

data class GlobalData (
    val cases : Int,
    val deaths : Int,
    val recovered : Int
)

data class CountriesData (

    val country : String,
    val cases : Int,
    val todayCases : Int,
    val deaths : Int,
    val todayDeaths : Int,
    val recovered : Int,
    val active : Int,
    val critical : Int
)