package com.oratakashi.covid19.ui.hospital

import com.oratakashi.covid19.data.model.localstorage.DataHospital

interface HospitalInterface {
    fun onSelected(data : DataHospital)
}