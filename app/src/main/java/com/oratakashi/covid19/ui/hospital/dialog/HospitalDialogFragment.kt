package com.oratakashi.covid19.ui.hospital.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataHospital


class HospitalDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hospital_dialog_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
    }

    @OnClick(R.id.llDirection) fun onDirection(){
        parent.onSelected("direction", data)
        dismiss()
    }

    @OnClick(R.id.llPhone) fun onPhone(){
        parent.onSelected("phone", data)
        dismiss()
    }

    companion object {
        lateinit var data : DataHospital
        lateinit var parent : HospitalDialogInterface
        fun newInstance(dataParent : DataHospital, interfaces : HospitalDialogInterface): HospitalDialogFragment =
            HospitalDialogFragment().apply {
                data = dataParent
                parent = interfaces
            }

    }
}