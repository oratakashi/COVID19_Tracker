package com.oratakashi.covid19.ui.about

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.model.contributor.DataContributor
import com.oratakashi.covid19.utils.AssetsManager
import org.json.JSONException

class AboutViewModel : ViewModel() {
    val dataAbout = MediatorLiveData<List<DataContributor>>()

    @Throws(JSONException::class)
    fun getContributor(context: Context){
        val data : MutableList<DataContributor> = ArrayList()

        for(i in 0 until AssetsManager.getJson(context, "contributor").length()){
            val json = AssetsManager.getJson(context, "contributor").getJSONObject(i)
            data.add(
                DataContributor(
                    json.getString("name"),
                    json.getString("photo"),
                    json.getString("role"),
                    json.getString("instagram"),
                    json.getString("link")
                )
            )
        }

        dataAbout.value = data
    }
}