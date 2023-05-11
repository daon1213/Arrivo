package com.daon.arrivo.data.repository

import com.daon.arrivo.domain.ArrivalInformation
import com.daon.arrivo.domain.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {

    val stations: Flow<List<Station>>

    suspend fun refreshStations()

    suspend fun getStationArrivals(stationName: String): List<ArrivalInformation>

    suspend fun updateStation(station: Station)
}