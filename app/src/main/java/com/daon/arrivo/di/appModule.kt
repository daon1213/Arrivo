package com.daon.arrivo.di

import android.app.Activity
import com.daon.arrivo.data.preference.PreferenceManager
import com.daon.arrivo.data.api.StationApi
import com.daon.arrivo.data.api.StationArrivalsApi
import com.daon.arrivo.data.api.StationStorageApi
import com.daon.arrivo.data.db.AppDatabase
import com.daon.arrivo.data.preference.SharedPreferenceManager
import com.daon.arrivo.data.repository.StationRepository
import com.daon.arrivo.data.repository.StationRepositoryImpl
import com.daon.arrivo.presentation.stationarrivals.StationArrivalsContract
import com.daon.arrivo.presentation.stationarrivals.StationArrivalsFragment
import com.daon.arrivo.presentation.stationarrivals.StationArrivalsPresenter
import com.daon.arrivo.presentation.stations.StationsContract
import com.daon.arrivo.presentation.stations.StationsFragment
import com.daon.arrivo.presentation.stations.StationsPresenter
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<StationArrivalsApi> {
        Retrofit.Builder().baseUrl(com.daon.arrivo.data.api.Url.SEOUL_DATA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }
    single<StationApi> { StationStorageApi(Firebase.storage) }

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get(), get()) }

    // Presentation
    scope<StationsFragment> {
        scoped<StationsContract.Presenter> { StationsPresenter(getSource(), get()) }
    }
    scope<StationArrivalsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource(), get(), get()) }
    }
}