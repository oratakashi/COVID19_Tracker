package com.oratakashi.covid19.ui.sortirdialog.sort_global

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.R
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface

class SortDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sortdialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
    }

    @OnClick(R.id.llAsc) fun onAsc(){
        interfaces.onSort("asc")
        dismiss()
    }

    @OnClick(R.id.llDesc) fun onDesc(){
        interfaces.onSort("desc")
        dismiss()
    }

    @OnClick(R.id.llAbjad) fun onAbjad(){
        interfaces.onSort("abjad")
        dismiss()
    }

    companion object {
        lateinit var interfaces: SortDialogInterface
        fun newInstance(parent : SortDialogInterface): SortDialogFragment =
            SortDialogFragment()
                .apply {
                interfaces = parent
            }

    }
}
