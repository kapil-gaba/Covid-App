package com.example.covidapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.covidapp.domain.GlobalData


@Entity
data class DatabaseGlobalData (
    @PrimaryKey
    val id : Int,
    val cases : Long,
    val deaths : Long,
    val recovered : Long
)


fun DatabaseGlobalData.asDomainModelGlobal() : GlobalData{
    return GlobalData(
            cases,deaths,recovered
        )
}