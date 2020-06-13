package com.oratakashi.covid19.ui.hospital.dialog

import com.oratakashi.covid19.data.model.localstorage.DataHospital

interface HospitalDialogInterface {
    fun onSelected(menu : String, data : DataHospital)
}