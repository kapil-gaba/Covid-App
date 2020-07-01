package com.example.covidapp.domain

data class GlobalData (
    val cases : Long,
    val deaths : Long,
    val recovered : Long
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

