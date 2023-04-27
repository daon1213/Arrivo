package com.daon.arrivo.presentation.stationarrivals

import com.daon.arrivo.domain.ArrivalInformation
import com.daon.arrivo.presentation.BasePresenter
import com.daon.arrivo.presentation.BaseView

interface StationArrivalsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)
    }

    interface Presenter : BasePresenter {

        fun fetchStationArrivals()

        fun toggleStationFavorite()
    }
}