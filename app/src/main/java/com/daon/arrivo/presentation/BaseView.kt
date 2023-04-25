package com.daon.arrivo.presentation

interface BaseView<PresenterT : BasePresenter> {

    val presenter: PresenterT
}