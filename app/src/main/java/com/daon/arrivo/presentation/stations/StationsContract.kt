package com.daon.arrivo.presentation.stations

import com.daon.arrivo.domain.Station
import com.daon.arrivo.presentation.BasePresenter
import com.daon.arrivo.presentation.BaseView

interface StationsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)
    }

    interface Presenter : BasePresenter {

        fun filterStations(query: String)

        fun toggleStationFavorite(station: Station)
    }
}