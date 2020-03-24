package com.oratakashi.covid19.ui.sortirdialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.R
import kotlinx.android.synthetic.main.fragment_sortlocal.*
import kotlinx.android.synthetic.main.fragment_sort_local_list_dialog_item.view.*


class SortLocalFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sortlocal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
    }

    @OnClick(R.id.llAscConfirm) fun onAscConfirm(){
        interfaces.onSort("asc_confirm")
        dismiss()
    }

    @OnClick(R.id.llDescConfirm) fun onDescConfirm(){
        interfaces.onSort("desc_confirm")
        dismiss()
    }

    @OnClick(R.id.llAscRecovered) fun onAscRecovered(){
        interfaces.onSort("asc_recovered")
        dismiss()
    }

    @OnClick(R.id.llDescRecovered) fun onDescRecovered(){
        interfaces.onSort("desc_recovered")
        dismiss()
    }

    @OnClick(R.id.llAscDeath) fun onAscDeath(){
        interfaces.onSort("asc_death")
        dismiss()
    }

    @OnClick(R.id.llDescDeath) fun onDescDeath(){
        interfaces.onSort("desc_death")
        dismiss()
    }

    @OnClick(R.id.llAbjad) fun onAbjad(){
        interfaces.onSort("abjad")
        dismiss()
    }

    companion object {
        lateinit var interfaces: SortDialogInterface
        fun newInstance(parent : SortDialogInterface): SortLocalFragment =
            SortLocalFragment().apply {
                interfaces = parent
            }

    }
}
