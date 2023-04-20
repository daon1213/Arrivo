package com.daon.arrivo.data.api

import com.daon.arrivo.data.db.entity.StationEntity
import com.daon.arrivo.data.db.entity.SubwayEntity

interface StationApi {

    suspend fun getStationDataUpdatedTimeMillis(): Long

    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}