package com.daon.arrivo.data.repository

import android.preference.PreferenceManager
import com.daon.arrivo.data.api.StationApi
import com.daon.arrivo.data.api.StationArrivalsApi
import com.daon.arrivo.data.api.response.mapper.toArrivalInformation
import com.daon.arrivo.data.db.StationDao
import com.daon.arrivo.data.db.entity.mapper.toStationEntity
import com.daon.arrivo.data.db.entity.mapper.toStations
import com.daon.arrivo.domain.ArrivalInformation
import com.daon.arrivo.domain.Station
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StationRepositoryImpl(
    private val stationArrivalsApi: StationArrivalsApi,
    private val stationApi: StationApi,
    private val stationDao: StationDao,
    private val preferenceManager: com.daon.arrivo.data.preference.PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : StationRepository {

    override val stations: Flow<List<Station>> =
        stationDao.getStationWithSubways()
            .distinctUntilChanged()
            .map { stations -> stations.toStations().sortedByDescending { it.isFavorited } }
            .flowOn(dispatcher)

    override suspend fun refreshStations() = withContext(dispatcher) {
        val fileUpdatedTimeMillis = stationApi.getStationDataUpdatedTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if (lastDatabaseUpdatedTimeMillis == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTimeMillis) {
            stationDao.insertStationSubways(stationApi.getStationSubways())
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMillis)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> = withContext(dispatcher) {
        stationArrivalsApi.getRealtimeStationArrivals(stationName)
            .body()
            ?.realtimeArrivalList
            ?.toArrivalInformation()
            ?.distinctBy { it.direction }
            ?.sortedBy { it.subway }
            ?: throw RuntimeException("도착 정보를 불러오는 데에 실패했습니다.")
    }

    override suspend fun updateStation(station: Station) = withContext(dispatcher) {
        stationDao.updateStation(station.toStationEntity())
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}