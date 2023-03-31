package com.daon.arrivo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daon.arrivo.data.db.entity.StationEntity
import com.daon.arrivo.data.db.entity.StationSubwayCrossRefEntity
import com.daon.arrivo.data.db.entity.SubwayEntity

@Database(
    entities = [StationEntity::class, SubwayEntity::class, StationSubwayCrossRefEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao

    companion object {

        private const val DATABASE_NAME = "station.db"

        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }
}