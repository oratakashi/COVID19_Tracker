package com.oratakashi.covid19.data.model.localstorage

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataTimeline (
    val date : String?,
    val case : Int?,
    val confirm : Int?,
    val recovered : Int?,
    val death : Int?
) : Parcelable