package com.oratakashi.covid19.ui.theme

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
import com.oratakashi.covid19.data.db.Sessions
import com.oratakashi.covid19.root.App
import kotlinx.android.synthetic.main.fragment_theme_dialog.*

class ThemeDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_theme_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
        when(App.sessions!!.getTheme()){
            "basic" -> {
                tvBasicStatus.visibility = View.VISIBLE
            }
            "advance" -> {
                tvAdvanceStatus.visibility = View.VISIBLE
            }
        }
    }

    @OnClick(R.id.cvBasic) fun onBasic(){
        parent.onThemeChanged("basic")
        dismiss()
    }

    @OnClick(R.id.cvAdvance) fun onAdvance(){
        parent.onThemeChanged("advance")
        dismiss()
    }

    companion object {
        lateinit var parent : ThemeInterfaces
        fun newInstance(interfaces: ThemeInterfaces): ThemeDialogFragment =
            ThemeDialogFragment().apply {
                parent = interfaces
            }

    }
}
