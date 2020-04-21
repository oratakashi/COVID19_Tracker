package com.oratakashi.covid19.ui.home.dialog

interface HomeDialogInterface {
    fun onSelected(province : String = "")
    interface adapter{
        fun onSelected(province : String)
    }
}