package com.oratakashi.covid19.ui.sortirdialog.sort_timeline

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
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import kotlinx.android.synthetic.main.fragment_sort_timeline.*


class SortTimelineFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sort_timeline, container, false)
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

    companion object {
        lateinit var interfaces: SortDialogInterface
        fun newInstance(parent : SortDialogInterface): SortTimelineFragment =
            SortTimelineFragment().apply {
                interfaces = parent
            }

    }
}
